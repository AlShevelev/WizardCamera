package com.shevelev.wizard_camera.main_activity.model.image_capture

import android.view.TextureView
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.main_activity.dto.ScreenOrientation

interface ImageCapture {
    val inProgress: Boolean

    suspend fun capture(textureView: TextureView, activeFilter: FilterCode, screenOrientation: ScreenOrientation): Boolean
}