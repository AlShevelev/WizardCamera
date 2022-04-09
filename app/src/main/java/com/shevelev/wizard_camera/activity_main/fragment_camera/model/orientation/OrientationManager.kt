package com.shevelev.wizard_camera.activity_main.fragment_camera.model.orientation

import androidx.camera.core.impl.ImageOutputConfig
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto.ScreenOrientation

interface OrientationManager {
    val screenOrientation: ScreenOrientation

    @ImageOutputConfig.RotationValue
    val surfaceRotation: Int

    fun start()

    fun stop()
}