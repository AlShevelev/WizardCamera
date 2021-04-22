package com.shevelev.wizard_camera.camera.filters

import android.content.Context
import android.opengl.GLES31
import com.shevelev.wizard_camera.camera.R
import com.shevelev.wizard_camera.common_entities.enums.Size
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.LegofiedFilterSettings

class LegofiedCameraFilter(context: Context) : CameraFilter(context, R.raw.legofied) {
    /**
     * Pass filter-specific arguments
     */
    override fun passSettingsParams(program: Int, settings: FilterSettings) {
        settings as LegofiedFilterSettings

        val blockSize = GLES31.glGetUniformLocation(program, "blockSize")

        val size = when(settings.size) {
            Size.SMALL -> 0.04f
            Size.NORMAL -> 0.03f
            Size.LARGE -> 0.02f
        }

        GLES31.glUniform1f(blockSize, size)
    }
}