package com.shevelev.wizard_camera.activity_main.fragment_camera.model.image_capture

import com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto.ScreenOrientation
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.photo_files.api.photo_shot_repository.model.StartCapturingResult
import java.io.File
import java.io.OutputStream

interface ImageCapture {
    val inProgress: Boolean

    /**
     * Starts capturing process
     * @return target file for an image
     */
    suspend fun startCapture(
        activeFilter: GlFilterSettings,
        screenOrientation: ScreenOrientation
    ) : StartCapturingResult?

    suspend fun captureCompleted(): Boolean
}