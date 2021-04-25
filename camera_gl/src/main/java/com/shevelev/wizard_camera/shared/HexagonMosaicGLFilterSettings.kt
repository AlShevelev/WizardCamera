package com.shevelev.wizard_camera.shared

import android.opengl.GLES31
import com.shevelev.wizard_camera.common_entities.enums.Size
import com.shevelev.wizard_camera.common_entities.filter_settings.HexagonMosaicFilterSettings

/**
 * A class for passing [HexagonMosaicFilterSettings] into an OGL filter
 */
class HexagonMosaicGLFilterSettings(
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