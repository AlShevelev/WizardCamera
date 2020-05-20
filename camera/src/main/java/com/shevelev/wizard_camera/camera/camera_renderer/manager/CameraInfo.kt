package com.shevelev.wizard_camera.camera.camera_renderer.manager

import android.graphics.Rect
import android.util.Range
import android.util.Rational

data class CameraInfo(
    val id: String,
    val isMeteringAreaAFSupported: Boolean,
    val sensorArraySize: Rect,
    val maxZoom: Float,
    val exposureRange : Range<Int>,
    val exposureStep: Rational
)