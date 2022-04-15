package com.shevelev.wizard_camera.shared

import android.opengl.GLES31
import com.shevelev.wizard_camera.core.common_entities.enums.Size
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.TrianglesMosaicFilterSettings
import java.nio.FloatBuffer

/**
 * A class for passing [TrianglesMosaicFilterSettings] into an OGL filter
 */
class TrianglesMosaicGLFilterSettings(
    settings: TrianglesMosaicFilterSettings
) : GLFilterSettingsBase<TrianglesMosaicFilterSettings>(settings) {
    /**
     * Passes params
     * @param program OGL program
     */
    override fun passSettingsParams(program: Int) {
        val blockSize = GLES31.glGetUniformLocation(program, "tileNum")

        val size = when(settings.size) {
            Size.SMALL -> floatArrayOf(50f, 100f, 1.0f)
            Size.NORMAL -> floatArrayOf(40f, 80f, 1.0f)
            Size.LARGE -> floatArrayOf(30f, 60f, 1.0f)
        }

        GLES31.glUniform2fv(blockSize, 1, FloatBuffer.wrap(size))
    }
}