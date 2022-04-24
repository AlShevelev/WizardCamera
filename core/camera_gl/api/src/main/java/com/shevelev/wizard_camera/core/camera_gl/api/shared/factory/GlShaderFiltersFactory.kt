package com.shevelev.wizard_camera.core.camera_gl.api.shared.factory

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.RawRes
import com.shevelev.wizard_camera.core.camera_gl.api.bitmap.filters.GlSurfaceShaderFilter
import com.shevelev.wizard_camera.core.camera_gl.api.shared.GLFilterSettings
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings

/**
 * An interface for creating filters, based on GL Shaders
 */
interface GlShaderFiltersFactory {
    /**
     * Create an OGL wrapper around a filter based on its settings
     */
    fun createGLFilterSettings(settings: GlFilterSettings, context: Context) : GLFilterSettings

    fun createFilter(bitmap: Bitmap, settings: GlFilterSettings): GlSurfaceShaderFilter

    @RawRes
    fun getFilterRes(code: GlFilterCode): Int
}