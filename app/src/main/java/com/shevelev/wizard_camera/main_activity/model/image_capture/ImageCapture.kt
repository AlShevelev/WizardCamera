package com.shevelev.wizard_camera.main_activity.model.image_capture

import android.view.TextureView
import com.shevelev.wizard_camera.camera.filter.FilterCode

interface ImageCapture {
    val inProgress: Boolean

    suspend fun capture(textureView: TextureView, activeFilter: FilterCode): Boolean
}