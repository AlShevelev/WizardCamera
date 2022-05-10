package com.shevelev.wizard_camera.core.photo_files.impl.photo_shot_repository.conventional

import android.content.Context
import androidx.core.content.FileProvider
import com.shevelev.wizard_camera.core.bitmaps.api.bitmaps.BitmapHelper
import com.shevelev.wizard_camera.core.bitmaps.api.orientation.BitmapOrientationCorrector
import com.shevelev.wizard_camera.core.bitmaps.api.orientation.Orientation
import com.shevelev.wizard_camera.core.build_info.api.BuildInfo
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.database.api.repositories.PhotoShotDbRepository
import com.shevelev.wizard_camera.core.photo_files.api.photo_shot_repository.PhotoShotRepository
import com.shevelev.wizard_camera.core.photo_files.api.photo_shot_repository.model.StartCapturingResult
import com.shevelev.wizard_camera.core.photo_files.impl.photo_shot_repository.PhotoShotRepositoryBase
import com.shevelev.wizard_camera.core.photo_files.impl.photo_shot_repository.conventional.media_scanner.MediaScanner
import com.shevelev.wizard_camera.core.utils.id.IdUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import timber.log.Timber
import java.io.File
import kotlin.math.absoluteValue

/**
 * An old-style repository, based on files
 */
internal class ConventionalFilesRepository(
    appContext: Context,
    appInfo: BuildInfo,
    private val mediaScanner: MediaScanner,
    private val bitmapOrientationCorrector: BitmapOrientationCorrector,
    bitmapHelper: BitmapHelper,
    photoShotRepository: PhotoShotDbRepository
) : PhotoShotRepositoryBase<File>(
    appContext,
    appInfo,
    bitmapHelper,
    photoShotRepository
), PhotoShotRepository {

    private val lock = Object()

    /**
     * Creates a file for a photo shot and returns its OutputStream
     */
    override suspend fun startCapturing(): StartCapturingResult =
        withContext(Dispatchers.IO) {
            val dir = getShotsDirectory()
            val file = File(dir, "${IdUtil.generateLongId().absoluteValue}.jpg")

            val stream = file.outputStream()

            val key = IdUtil.generateLongId()
            putState(key, stream, file)

            StartCapturingResult(key, stream)
        }

    /**
     * Completes capturing process
     * @param key a value of [StartCapturingResult.key]
     */
    override suspend fun completeCapturing(key: Long, filter: GlFilterSettings): PhotoShot? =
        withContext(Dispatchers.IO) {
            try {
                extractState(key)?.let { state ->
                    val stream = state.first
                    val file = state.second

                    stream.close()

                    bitmapOrientationCorrector.getOrientation(file)?.let { orientation ->
                        if(orientation != Orientation.NORMAL) {
                            bitmapHelper.update(file) { bitmap ->
                                bitmapOrientationCorrector.rotate(bitmap, orientation)
                            }
                        }
                    }

                    val contentUri = mediaScanner.processNewShot(file)
                    val fileUri = FileProvider.getUriForFile(appContext, "${appInfo.appId}.file_provider", file)

                    val shot = PhotoShot(
                        id = IdUtil.generateLongId(),
                        contentUri = fileUri,
                        fileName = file.name,
                        created = ZonedDateTime.now(),
                        filter = filter,
                        mediaContentUri = contentUri
                    )

                    photoShotRepository.create(shot)

                    shot
                }
            } catch (ex: Exception) {
                Timber.e(ex)
                null
            }
        }

    /**
     * Completes capturing process (without coroutines, in a background thread)
     * @param key a value of [StartCapturingResult.key]
     */
    override fun completeCapturingForService(key: Long, filter: GlFilterSettings) {
        try {
            extractState(key)?.let { state ->
                val stream = state.first
                val file = state.second

                stream.close()

                bitmapOrientationCorrector.getOrientation(file)?.let { orientation ->
                    if (orientation != Orientation.NORMAL) {
                        bitmapHelper.update(file) { bitmap ->
                            bitmapOrientationCorrector.rotate(bitmap, orientation)
                        }
                    }
                }

                mediaScanner.processNewShot(file) { contentUri ->
                    val fileUri = FileProvider.getUriForFile(appContext, "${appInfo.appId}.file_provider", file)

                    val shot = PhotoShot(
                        id = IdUtil.generateLongId(),
                        contentUri = fileUri,
                        fileName = file.name,
                        created = ZonedDateTime.now(),
                        filter = filter,
                        mediaContentUri = contentUri
                    )

                    photoShotRepository.create(shot)

                    synchronized(lock) {
                        lock.notify()
                    }
                }

                synchronized(lock) {
                    lock.wait(3000L)
                }
            }
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

    /**
     * Removes a given shot
     */
    override suspend fun removeShot(photoShot: PhotoShot) {
        val file = File(getShotsDirectory(), photoShot.fileName!!)

        withContext(Dispatchers.IO) {
            photoShotRepository.deleteById(photoShot.id)
            file.delete()
        }

        mediaScanner.processDeletedShot(file)
    }

    @Suppress("DEPRECATION")
    private fun getShotsDirectory(): File {
        val dir = File(appContext.externalMediaDirs[0], appInfo.appName)
        if(!dir.exists()) {
            dir.mkdir()
        }

        return dir
    }
}