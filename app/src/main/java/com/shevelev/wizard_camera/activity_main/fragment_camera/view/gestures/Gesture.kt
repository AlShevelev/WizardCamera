package com.shevelev.wizard_camera.activity_main.fragment_camera.view.gestures

import android.graphics.PointF
import android.util.SizeF

sealed class Gesture

/**
 * User's tap
 * @param touchPoint a coordinates of a tap
 * @param touchAreaSize size of the tapped view
 */
data class Tap(val touchPoint: PointF, val touchAreaSize: SizeF) : Gesture()

data class Pinch(val scaleFactor: Float) : Gesture()
