package com.shevelev.wizard_camera.activity_main.fragment_camera.model.orientation

import com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto.ScreenOrientation

interface OrientationManager {
    val screenOrientation: ScreenOrientation

    fun start()

    fun stop()
}