package com.shevelev.wizard_camera.activity_main.fragment_camera.model.orientation

import android.content.Context
import android.hardware.SensorManager
import android.view.OrientationEventListener
import android.view.Surface.*
import androidx.camera.core.impl.ImageOutputConfig
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto.ScreenOrientation

class OrientationManagerImpl(
    context: Context
) : OrientationEventListener(context, SensorManager.SENSOR_DELAY_NORMAL),
    OrientationManager {

    override var screenOrientation: ScreenOrientation = ScreenOrientation.PORTRAIT
        private set

    @ImageOutputConfig.RotationValue
    override var surfaceRotation: Int = ROTATION_0
        private set

    override fun start() = enable()

    override fun stop() = disable()

    override fun onOrientationChanged(orientation: Int) {
        if (orientation == -1) {
            return
        }

         val newOrientation = when (orientation) {
             in 60 until 140 -> ScreenOrientation.REVERSED_LANDSCAPE
             in 140 until 220 -> ScreenOrientation.REVERSED_PORTRAIT
             in 220 until 300 -> ScreenOrientation.LANDSCAPE
             else -> ScreenOrientation.PORTRAIT
         }

        if(newOrientation != screenOrientation){
            screenOrientation = newOrientation
        }

        val newRotation = when(orientation) {
            in 225 until 315 -> ROTATION_90
            in 135 until 225 -> ROTATION_180
            in 45 until 135 -> ROTATION_270
            else -> ROTATION_0
        }

        if(newRotation != surfaceRotation) {
            surfaceRotation = newRotation
        }
    }
}