package com.shevelev.wizard_camera.camera.filters

import android.content.Context
import android.opengl.GLES31
import com.shevelev.wizard_camera.camera.R
import com.shevelev.wizard_camera.common_entities.enums.Size
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.HexagonMosaicFilterSettings

class HexagonMosaicCameraFilter(context: Context) : CameraFilter(context, R.raw.hexagon_mosaic) {
    /**
     * Pass filter-specific arguments
     */
    override fun passSettingsParams(program: Int, settings: FilterSettings) {
        settings as HexagonMosaicFilterSettings

        val blockSize = GLES31.glGetUniformLocation(program, "blockSize")

        val size = when(settings.size) {
            Size.SMALL -> 0.0075f
            Size.NORMAL -> 0.01f
            Size.LARGE -> 0.015f
        }

        GLES31.glUniform1f(blockSize, size)
    }
}