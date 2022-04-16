package com.shevelev.wizard_camera.core.camera_gl.shared

import android.opengl.GLES31
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.EdgeDetectionFilterSettings

/**
 * A class for passing [EdgeDetectionFilterSettings] into an OGL filter
 */
class EdgeDetectionGLFilterSettings(
    settings: EdgeDetectionFilterSettings
) : GLFilterSettingsBase<EdgeDetectionFilterSettings>(settings) {
    /**
     * Passes params
     * @param program OGL program
     */
    override fun passSettingsParams(program: Int) {
        val inverted = GLES31.glGetUniformLocation(program, "inverted")
        GLES31.glUniform1i(inverted, if(settings.isInverted) 1 else 0)
    }
}