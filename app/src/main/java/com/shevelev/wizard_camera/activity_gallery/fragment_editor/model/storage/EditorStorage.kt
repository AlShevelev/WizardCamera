package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage

import android.graphics.Bitmap
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings

interface EditorStorage {
    val displayedImage: Bitmap

    var lastUsedGlFilter: GlFilterSettings?

    val isSourceImageDisplayed: Boolean

    val isUpdated: Boolean

    suspend fun initImage()

    fun switchToSourceImage()

    suspend fun switchToHistogramEqualizedImage()

    fun getUsedFilter(code: GlFilterCode): GlFilterSettings?

    fun memorizeUsedFilter(filter: GlFilterSettings)

    fun onUpdate()
}