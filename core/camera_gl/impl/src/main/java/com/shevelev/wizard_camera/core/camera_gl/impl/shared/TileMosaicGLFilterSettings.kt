package com.shevelev.wizard_camera.core.camera_gl.impl.shared

import android.opengl.GLES31
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.TileMosaicFilterSettings

/**
 * A class for passing [TrianglesMosaicGLFilterSettings] into an OGL filter
 */
class TileMosaicGLFilterSettings(
    settings: TileMosaicFilterSettings
) : GLFilterSettingsBase<TileMosaicFilterSettings>(settings) {
    /**
     * Passes params
     * @param program OGL program
     */
    override fun passSettingsParams(program: Int) {
        val tileSize = GLES31.glGetUniformLocation(program, "maxTileSize")
        GLES31.glUniform1f(tileSize, settings.tileSize.toFloat())

        val borderSize = GLES31.glGetUniformLocation(program, "borderSize")
        GLES31.glUniform1f(borderSize, settings.borderSize.toFloat())
    }
}