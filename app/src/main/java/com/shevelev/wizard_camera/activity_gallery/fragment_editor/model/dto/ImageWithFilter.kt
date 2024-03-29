package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.dto

import android.graphics.Bitmap
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings

data class ImageWithFilter(
    val image: Bitmap,
    val settings: GlFilterSettings
)