package com.shevelev.wizard_camera.camera.camera_settings_repository

import android.graphics.Rect
import android.util.Range
import android.util.Rational
import android.util.Size

data class CameraInfo(
    val id: String,
    val isMeteringAreaAFSupported: Boolean,
    val sensorArraySize: Rect,
    val maxZoom: Float,
    val exposureRange : Range<Int>,
    val exposureStep: Rational,

    val outputSize: List<Size>,
    val optimalOutputSize: Size
)