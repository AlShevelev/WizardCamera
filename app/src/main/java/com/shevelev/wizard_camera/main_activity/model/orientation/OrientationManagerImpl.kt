package com.shevelev.wizard_camera.main_activity.model.orientation

import android.content.Context
import android.hardware.SensorManager
import android.view.OrientationEventListener
import com.shevelev.wizard_camera.main_activity.dto.ScreenOrientation
import javax.inject.Inject

class OrientationManagerImpl
@Inject
constructor(
    context: Context
) : OrientationEventListener(context, SensorManager.SENSOR_DELAY_NORMAL),
    OrientationManager {

    override var screenOrientation: ScreenOrientation = ScreenOrientation.PORTRAIT
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
    }
}