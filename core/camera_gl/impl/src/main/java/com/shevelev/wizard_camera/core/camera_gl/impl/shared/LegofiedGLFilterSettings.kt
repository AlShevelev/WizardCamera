package com.shevelev.wizard_camera.core.camera_gl.impl.shared

import android.opengl.GLES31
import com.shevelev.wizard_camera.core.common_entities.enums.Size
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.LegofiedFilterSettings

/**
 * A class for passing [LegofiedFilterSettings] into an OGL filter
 */
internal class LegofiedGLFilterSettings(
    settings: LegofiedFilterSettings
) : GLFilterSettingsBase<LegofiedFilterSettings>(settings) {
    /**
     * Passes params
     * @param program OGL program
     */
    override fun passSettingsParams(program: Int) {
        val blockSize = GLES31.glGetUniformLocation(program, "blockSize")

        val size = when(settings.size) {
            Size.SMALL -> 0.04f
            Size.NORMAL -> 0.03f
            Size.LARGE -> 0.02f
        }

        GLES31.glUniform1f(blockSize, size)
    }
}