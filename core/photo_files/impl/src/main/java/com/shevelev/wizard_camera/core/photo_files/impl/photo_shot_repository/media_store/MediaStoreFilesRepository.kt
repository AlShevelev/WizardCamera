package com.shevelev.wizard_camera.core.photo_files.impl.photo_shot_repository.media_store

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
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
import com.shevelev.wizard_camera.core.utils.id.IdUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import timber.log.Timber
import kotlin.math.absoluteValue

/**
 * An new-style repository, based on MediaStore
 */
@RequiresApi(Build.VERSION_CODES.Q)
internal class MediaStoreFilesRepository(
    appContext: Context,
    appInfo: BuildInfo,
    private val bitmapOrientationCorrector: BitmapOrientationCorrector,
    bitmapHelper: BitmapHelper,
    photoShotRepository: PhotoShotDbRepository
) : PhotoShotRepositoryBase<Uri>(
    appContext,
    appInfo,
    bitmapHelper,
    photoShotRepository
),  PhotoShotRepository {
    /**
     * Creates a file for a photo shot and returns its OutputStream
     */
    override suspend fun startCapturing(): StartCapturingResult? =
        withContext(Dispatchers.IO) {
            val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)

            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "${IdUtil.generateLongId().absoluteValue}.jpg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/${appInfo.appName}")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }

            val resolver = appContext.contentResolver
            resolver.insert(collection, values)?.let { uri ->
                resolver.openOutputStream(uri)?.let { stream ->
                    val key = IdUtil.generateLongId()
                    putState(key, stream, uri)
                    StartCapturingResult(key, stream)
                }
            }
        }

    /**
     * Completes capturing process
     * @param key a value of [StartCapturingResult.key]
     */
    override suspend fun completeCapturing(key: Long, filter: GlFilterSettings): PhotoShot? =
        withContext(Dispatchers.IO) {
            completeCapturingInternal(key, filter)
        }

    /**
     * Completes capturing process (without coroutines, in a background thread)
     * @param key a value of [StartCapturingResult.key]
     */
    override fun completeCapturingForService(key: Long, filter: GlFilterSettings) {
        completeCapturingInternal(key, filter)
    }

    /**
     * Removes a given shot
     */
    override suspend fun removeShot(photoShot: PhotoShot) =
        withContext(Dispatchers.IO) {
            appContext.contentResolver.delete(photoShot.contentUri, null, null)

            photoShotRepository.deleteById(photoShot.id)
        }

    private fun completeCapturingInternal(key: Long, filter: GlFilterSettings): PhotoShot? =
        try {
            extractState(key)?.let { state ->
                val stream = state.first
                val uri = state.second

                stream.close()

                bitmapOrientationCorrector.getOrientation(uri)?.let { orientation ->
                    if(orientation != Orientation.NORMAL) {
                        bitmapHelper.update(uri) { bitmap ->
                            bitmapOrientationCorrector.rotate(bitmap, orientation)
                        }
                    }
                }

                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.IS_PENDING, 0)
                }
                appContext.contentResolver.update(uri, values, null, null)

                val shot = PhotoShot(
                    id = IdUtil.generateLongId(),
                    contentUri = uri,
                    fileName = null,
                    created = ZonedDateTime.now(),
                    filter = filter,
                    mediaContentUri = null
                )

                photoShotRepository.create(shot)

                shot
            }
        } catch (ex: Exception) {
            Timber.e(ex)
            null
        }
}