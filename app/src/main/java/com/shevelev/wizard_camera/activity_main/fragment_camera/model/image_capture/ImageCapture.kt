package com.shevelev.wizard_camera.activity_main.fragment_camera.model.image_capture

import com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto.ScreenOrientation
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings
import java.io.File

interface ImageCapture {
    val inProgress: Boolean

    /**
     * Starts capturing process
     * @return target file for an image
     */
    suspend fun startCapture(activeFilter: GlFilterSettings, screenOrientation: ScreenOrientation): File?

    suspend fun captureCompleted()
}