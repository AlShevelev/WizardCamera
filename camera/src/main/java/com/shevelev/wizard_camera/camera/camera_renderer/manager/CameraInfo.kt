package com.shevelev.wizard_camera.camera.camera_renderer.manager

import android.graphics.Rect

data class CameraInfo(
    val id: String,
    val isMeteringAreaAFSupported: Boolean,
    val sensorArraySize: Rect,
    val maxZoom: Float
)