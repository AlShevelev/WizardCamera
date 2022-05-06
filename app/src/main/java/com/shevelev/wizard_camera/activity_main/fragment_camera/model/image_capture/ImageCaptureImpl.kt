package com.shevelev.wizard_camera.activity_main.fragment_camera.model.image_capture

import com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto.ScreenOrientation
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.photo_files.api.photo_shot_repository.PhotoShotRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.OutputStream

class ImageCaptureImpl
constructor(
    private val photoShotRepository: PhotoShotRepository
) : ImageCapture {

    override val inProgress: Boolean
        get() = targetStream != null

    private var targetStream: OutputStream? = null

    private var activeFilter: GlFilterSettings? = null

    /**
     * Starts capturing process
     * @return target file for an image
     */
    override suspend fun startCapture(activeFilter: GlFilterSettings, screenOrientation: ScreenOrientation): OutputStream? {
        targetStream = try {
            this.activeFilter = activeFilter

            withContext(Dispatchers.IO) {
                photoShotRepository.startCapturing()
            }
        } catch (ex: Exception) {
            Timber.e(ex)
            null
        }

        return targetStream
    }

    override suspend fun captureCompleted(): Boolean {
        val isSuccess = withContext(Dispatchers.IO) {
            targetStream?.let { targetStream ->
                photoShotRepository.completeCapturing(targetStream, activeFilter!!) != null
            }
                ?: false
        }

        targetStream = null
        activeFilter = null

        return isSuccess
    }
}