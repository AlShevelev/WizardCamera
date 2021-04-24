package com.shevelev.wizard_camera.bitmap.filters.fragment

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES31
import android.util.Size
import com.shevelev.wizard_camera.bitmap.filters.GLSurfaceShaderFilterBase
import com.shevelev.wizard_camera.camera.R
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.NewspaperFilterSettings

class NewspaperSurfaceFilter(
    context: Context,
    bitmap: Bitmap,
    settings: FilterSettings,
    screenSize: Size
) : GLSurfaceShaderFilterBase(
    context,
    bitmap,
    R.raw.newspaper,
    screenSize,
    settings) {

    override fun useSettings(settings: FilterSettings) {
        settings as NewspaperFilterSettings

        val grayscale = GLES31.glGetUniformLocation(program, "grayscale")
        GLES31.glUniform1i(grayscale, if (settings.isGrayscale) 1 else 0)
    }
}