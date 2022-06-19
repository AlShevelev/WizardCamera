package com.shevelev.wizard_camera.activity_main.fragment_camera.model.image_capture

import android.content.Context
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto.ScreenOrientation
import com.shevelev.wizard_camera.capturing_service.PhotoShotCaptureCompleteService
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.photo_files.api.photo_shot_repository.PhotoShotRepository
import com.shevelev.wizard_camera.core.photo_files.api.photo_shot_repository.model.StartCapturingResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class ImageCaptureImpl(
    private val appContext: Context,
    private val photoShotRepository: PhotoShotRepository
) : ImageCapture {

    override val inProgress: Boolean
        get() = startCapturingResult != null

    private var startCapturingResult: StartCapturingResult? = null

    private var activeFilter: GlFilterSettings? = null

    /**
     * Starts capturing process
     * @return target file for an image
     */
    override suspend fun startCapture(
        activeFilter: GlFilterSettings,
        screenOrientation: ScreenOrientation
    ): StartCapturingResult? {
        startCapturingResult = try {
            this.activeFilter = activeFilter

            withContext(Dispatchers.IO) {
                photoShotRepository.startCapturing()
            }
        } catch (ex: Exception) {
            Timber.e(ex)
            null
        }

        return startCapturingResult
    }

    override suspend fun captureCompleted(): Boolean =
        try {
            startCapturingResult?.let { result ->
                PhotoShotCaptureCompleteService.completeCapturing(appContext, result.key, activeFilter!!)
            }

            startCapturingResult = null
            activeFilter = null

            true
        } catch (ex: Exception) {
            Timber.e(ex)
            false
        }
}