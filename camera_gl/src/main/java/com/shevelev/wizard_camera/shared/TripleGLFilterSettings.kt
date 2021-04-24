package com.shevelev.wizard_camera.shared

import android.opengl.GLES31
import com.shevelev.wizard_camera.common_entities.filter_settings.TripleFilterSettings

/**
 * A class for passing [TripleFilterSettings] into an OGL filter
 */
class TripleGLFilterSettings(
    settings: TripleFilterSettings
) : GLFilerSettingsBase<TripleFilterSettings>(settings) {
    /**
     * Passes params
     * @param program OGL program
     */
    override fun passSettingsParams(program: Int) {
        val horizontal = GLES31.glGetUniformLocation(program, "horizontal")
        GLES31.glUniform1i(horizontal, if(settings.isHorizontal) 1 else 0)
    }
}