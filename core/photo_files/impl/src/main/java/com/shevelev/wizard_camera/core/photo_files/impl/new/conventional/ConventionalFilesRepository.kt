package com.shevelev.wizard_camera.core.photo_files.impl.new.conventional

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import com.shevelev.wizard_camera.core.bitmaps.api.bitmaps.BitmapHelper
import com.shevelev.wizard_camera.core.bitmaps.api.orientation.BitmapOrientationCorrector
import com.shevelev.wizard_camera.core.bitmaps.api.orientation.Orientation
import com.shevelev.wizard_camera.core.build_info.api.BuildInfo
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.database.api.repositories.PhotoShotDbRepository
import com.shevelev.wizard_camera.core.photo_files.impl.MediaScanner
import com.shevelev.wizard_camera.core.photo_files.api.new.PhotoShotRepository
import com.shevelev.wizard_camera.core.utils.id.IdUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import timber.log.Timber
import java.io.File
import java.io.OutputStream
import kotlin.math.absoluteValue

/**
 * An old-style repository, based on files
 */
internal class ConventionalFilesRepository(
    private val appContext: Context,
    private val appInfo: BuildInfo,
    private val mediaScanner: MediaScanner,
    private val bitmapOrientationCorrector: BitmapOrientationCorrector,
    private val bitmapHelper: BitmapHelper,
    private val photoShotRepository: PhotoShotDbRepository
) : PhotoShotRepository {

    private val filesMap = mutableMapOf<OutputStream, File>()

    /**
     * Creates a file for a photo shot and returns its OutputStream
     */
    override suspend fun startCapturing(): OutputStream =
        withContext(Dispatchers.IO) {
            val dir = getShotsDirectory()
            val file = File(dir, "${IdUtil.generateLongId().absoluteValue}.jpg")

            val stream = file.outputStream()

            putFile(stream, file)

            stream
        }

    /**
     * Completes capturing process
     * @param stream a stream created in [startCapturing] function with a stored image data
     */
    override suspend fun completeCapturing(stream: OutputStream, filter: GlFilterSettings): PhotoShot? =
        withContext(Dispatchers.IO) {
            try {
                stream.close()

                extractFile(stream)?.let { file ->
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
                        fileContentUri = fileUri,
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
     * Saves a bitmap into a temporary storage and returns content Uri for the saved bitmap
     */
    override suspend fun saveBitmapToTempStorage(bitmap: Bitmap): Uri {
        val file = withContext(Dispatchers.IO) {
            File(appContext.cacheDir, "${IdUtil.generateLongId().absoluteValue}.jpg").also {
                bitmapHelper.save(bitmap, it)
            }
        }

        return FileProvider.getUriForFile(appContext, "${appInfo.appId}.file_provider", file)
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

    /**
     * Updates a given shot by a new bitmap or/and a new filter
     * @return updated shot
     */
    override suspend fun updateShot(bitmap: Bitmap, filter: GlFilterSettings, updatedShot: PhotoShot): PhotoShot =
        withContext(Dispatchers.IO) {
            bitmapHelper.save(bitmap, updatedShot.fileContentUri)

            val shotToSave = updatedShot.copy(filter = filter)
            photoShotRepository.update(shotToSave)

            shotToSave
        }

    private fun getShotsDirectory(): File {
        val dir = File(appContext.externalMediaDirs[0], appInfo.appName)
        if(!dir.exists()) {
            dir.mkdir()
        }

        return dir
    }

    @Synchronized
    private fun extractFile(stream: OutputStream): File? = filesMap.remove(stream)

    @Synchronized
    private fun putFile(stream: OutputStream, file: File) {
        filesMap[stream] = file
    }
}