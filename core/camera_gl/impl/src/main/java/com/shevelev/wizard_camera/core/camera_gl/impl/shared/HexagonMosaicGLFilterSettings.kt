package com.shevelev.wizard_camera.core.camera_gl.impl.shared

import android.opengl.GLES31
import com.shevelev.wizard_camera.core.common_entities.enums.Size
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.HexagonMosaicFilterSettings

/**
 * A class for passing [HexagonMosaicFilterSettings] into an OGL filter
 */
internal class HexagonMosaicGLFilterSettings(
    settings: HexagonMosaicFilterSettings
) : GLFilterSettingsBase<HexagonMosaicFilterSettings>(settings) {
    /**
     * Passes params
     * @param program OGL program
     */
    override fun passSettingsParams(program: Int) {
        val blockSize = GLES31.glGetUniformLocation(program, "blockSize")

        val size = when(settings.size) {
            Size.SMALL -> 0.0075f
            Size.NORMAL -> 0.01f
            Size.LARGE -> 0.015f
        }

        GLES31.glUniform1f(blockSize, size)
    }
}