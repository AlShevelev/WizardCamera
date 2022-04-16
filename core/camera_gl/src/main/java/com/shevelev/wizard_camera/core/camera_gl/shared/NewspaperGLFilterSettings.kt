package com.shevelev.wizard_camera.core.camera_gl.shared

import android.opengl.GLES31
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.NewspaperFilterSettings

/**
 * A class for passing [NewspaperFilterSettings] into an OGL filter
 */
class NewspaperGLFilterSettings(
    settings: NewspaperFilterSettings
) : GLFilterSettingsBase<NewspaperFilterSettings>(settings) {
    /**
     * Passes params
     * @param program OGL program
     */
    override fun passSettingsParams(program: Int) {
        val grayscale = GLES31.glGetUniformLocation(program, "grayscale")
        GLES31.glUniform1i(grayscale, if(settings.isGrayscale) 1 else 0)
    }
}