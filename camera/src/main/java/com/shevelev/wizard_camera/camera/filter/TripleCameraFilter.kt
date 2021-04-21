package com.shevelev.wizard_camera.camera.filter

import android.content.Context
import android.opengl.GLES31
import com.shevelev.wizard_camera.camera.R
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.TripleFilterSettings

class TripleCameraFilter(context: Context) : CameraFilter(context, R.raw.triple) {
    /**
     * Pass filter-specific arguments
     */
    override fun passSettingsParams(program: Int, settings: FilterSettings) {
        settings as TripleFilterSettings

        val horizontal = GLES31.glGetUniformLocation(program, "horizontal")
        GLES31.glUniform1i(horizontal, if(settings.isHorizontal) 1 else 0)
    }
}