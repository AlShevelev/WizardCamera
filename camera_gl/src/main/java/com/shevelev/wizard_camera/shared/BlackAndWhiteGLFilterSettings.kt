package com.shevelev.wizard_camera.shared

import android.opengl.GLES31
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.BlackAndWhiteFilterSettings

/**
 * A class for passing [BlackAndWhiteFilterSettings] into an OGL filter
 */
class BlackAndWhiteGLFilterSettings(
    settings: BlackAndWhiteFilterSettings
) : GLFilterSettingsBase<BlackAndWhiteFilterSettings>(settings) {
    /**
     * Passes params
     * @param program OGL program
     */
    override fun passSettingsParams(program: Int) {
        val inverted = GLES31.glGetUniformLocation(program, "inverted")
        GLES31.glUniform1i(inverted, if(settings.isInverted) 1 else 0)
    }
}