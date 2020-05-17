package com.shevelev.wizard_camera.main_activity.model.orientation

import com.shevelev.wizard_camera.main_activity.dto.ScreenOrientation

interface OrientationManager {
    val screenOrientation: ScreenOrientation

    fun start()

    fun stop()
}