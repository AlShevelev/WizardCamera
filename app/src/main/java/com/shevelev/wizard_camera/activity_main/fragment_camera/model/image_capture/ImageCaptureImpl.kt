package com.shevelev.wizard_camera.activity_main.fragment_camera.model.image_capture

import com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto.ScreenOrientation
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.photo_files.api.new.PhotoFilesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.OutputStream

class ImageCaptureImpl
constructor(
    private val photoFilesRepository: PhotoFilesRepository
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
                photoFilesRepository.startCapturing()
            }
        } catch (ex: Exception) {
            Timber.e(ex)
            null
        }

        return targetStream
    }

    override suspend fun captureCompleted() {
        withContext(Dispatchers.IO) {
            targetStream?.let { targetStream ->
                photoFilesRepository.completeCapturing(targetStream, activeFilter!!)
            }
        }

        targetStream = null
        activeFilter = null
    }
}