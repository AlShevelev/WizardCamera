package com.shevelev.wizard_camera.main_activity.view.gestures

import android.graphics.PointF
import android.util.SizeF

sealed class Gesture {
    object FlingRight : Gesture()
    object FlingLeft : Gesture()

        data class Tap(val touchPoint: PointF, val touchAreaSize: SizeF) : Gesture()
}