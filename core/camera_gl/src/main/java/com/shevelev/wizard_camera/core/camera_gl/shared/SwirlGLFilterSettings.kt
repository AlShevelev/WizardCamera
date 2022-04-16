package com.shevelev.wizard_camera.core.camera_gl.shared

import android.opengl.GLES31
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.SwirlFilterSettings

/**
 * A class for passing [SwirlFilterSettings] into an OGL filter
 */
class SwirlGLFilterSettings(
    settings: SwirlFilterSettings
) : GLFilterSettingsBase<SwirlFilterSettings>(settings) {
    /**
     * Passes params
     * @param program OGL program
     */
    override fun passSettingsParams(program: Int) {
        val rotation = GLES31.glGetUniformLocation(program, "rotation")
        var rotationValue = 11f - settings.rotation.toFloat()
        if(settings.invertRotation) {
            rotationValue = 1f / rotationValue
        }
        GLES31.glUniform1f(rotation, rotationValue)

        val radius = GLES31.glGetUniformLocation(program, "radiusRatio")
        GLES31.glUniform1f(radius, settings.radius.toFloat() / 10f)
    }
}