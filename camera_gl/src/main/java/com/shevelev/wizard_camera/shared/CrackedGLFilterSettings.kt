package com.shevelev.wizard_camera.shared

import android.opengl.GLES31
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.CrackedFilterSettings

/**
 * A class for passing [CrackedFilterSettings] into an OGL filter
 */
class CrackedGLFilterSettings(
    settings: CrackedFilterSettings
) : GLFilterSettingsBase<CrackedFilterSettings>(settings) {
    /**
     * Passes params
     * @param program OGL program
     */
    override fun passSettingsParams(program: Int) {
        val shardsCount = GLES31.glGetUniformLocation(program, "shardsCount")
        GLES31.glUniform1i(shardsCount, settings.shards)

        val randomA = GLES31.glGetUniformLocation(program, "randomA")
        GLES31.glUniform1f(randomA, settings.randomA)

        val randomB = GLES31.glGetUniformLocation(program, "randomB")
        GLES31.glUniform1f(randomB, settings.randomB)

        val randomC = GLES31.glGetUniformLocation(program, "randomC")
        GLES31.glUniform1f(randomC, settings.randomC)
    }
}