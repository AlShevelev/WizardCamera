package com.shevelev.wizard_camera.camera.filter

import android.content.Context
import android.opengl.GLES31
import com.shevelev.wizard_camera.camera.R
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.NewspaperFilterSettings

class NewspaperCameraFilter(context: Context) : CameraFilter(context, R.raw.newspaper) {
    /**
     * Pass filter-specific arguments
     */
    override fun passSettingsParams(program: Int, settings: FilterSettings) {
        settings as NewspaperFilterSettings

        val grayscale = GLES31.glGetUniformLocation(program, "grayscale")
        GLES31.glUniform1i(grayscale, if(settings.isGrayscale) 1 else 0)
    }
}