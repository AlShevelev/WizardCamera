package com.shevelev.wizard_camera.camera.filters

import android.content.Context
import android.opengl.GLES31
import com.shevelev.wizard_camera.camera.R
import com.shevelev.wizard_camera.common_entities.filter_settings.BlackAndWhiteFilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings

class BlackAndWhiteCameraFilter(context: Context) : CameraFilter(context, R.raw.black_and_white) {
    /**
     * Pass filter-specific arguments
     */
    override fun passSettingsParams(program: Int, settings: FilterSettings) {
        settings as BlackAndWhiteFilterSettings

        val inverted = GLES31.glGetUniformLocation(program, "inverted")
        GLES31.glUniform1i(inverted, if(settings.isInverted) 1 else 0)
    }
}