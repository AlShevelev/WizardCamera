package com.shevelev.wizard_camera.core.camera_gl.impl.shared

import android.opengl.GLES31
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.TripleFilterSettings

/**
 * A class for passing [TripleFilterSettings] into an OGL filter
 */
internal class TripleGLFilterSettings(
    settings: TripleFilterSettings
) : GLFilterSettingsBase<TripleFilterSettings>(settings) {
    /**
     * Passes params
     * @param program OGL program
     */
    override fun passSettingsParams(program: Int) {
        val horizontal = GLES31.glGetUniformLocation(program, "horizontal")
        GLES31.glUniform1i(horizontal, if(settings.isHorizontal) 1 else 0)
    }
}