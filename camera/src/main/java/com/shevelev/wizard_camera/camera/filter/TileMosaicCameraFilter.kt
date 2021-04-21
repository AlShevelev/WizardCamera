package com.shevelev.wizard_camera.camera.filter

import android.content.Context
import android.opengl.GLES31
import com.shevelev.wizard_camera.camera.R
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.TileMosaicFilterSettings

class TileMosaicCameraFilter(context: Context) : CameraFilter(context, R.raw.tile_mosaic) {
    /**
     * Pass filter-specific arguments
     */
    override fun passSettingsParams(program: Int, settings: FilterSettings) {
        settings as TileMosaicFilterSettings

        val tileSize = GLES31.glGetUniformLocation(program, "maxTileSize")
        GLES31.glUniform1f(tileSize, settings.tileSize.toFloat())

        val borderSize = GLES31.glGetUniformLocation(program, "borderSize")
        GLES31.glUniform1f(borderSize, settings.borderSize.toFloat())
    }
}