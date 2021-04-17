package com.shevelev.wizard_camera.filter

import android.content.Context
import android.opengl.GLES31
import com.shevelev.wizard_camera.camera.R
import com.shevelev.wizard_camera.common_entities.enums.Size
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.TrianglesMosaicFilterSettings
import java.nio.FloatBuffer

class TrianglesMosaicCameraFilter(context: Context) : CameraFilter(context, R.raw.triangles_mosaic) {
    /**
     * Pass filter-specific arguments
     */
    override fun passSettingsParams(program: Int, settings: FilterSettings) {
        settings as TrianglesMosaicFilterSettings

        val blockSize = GLES31.glGetUniformLocation(program, "tileNum")

        val size = when(settings.size) {
            Size.SMALL -> floatArrayOf(50f, 100f, 1.0f)
            Size.NORMAL -> floatArrayOf(40f, 80f, 1.0f)
            Size.LARGE -> floatArrayOf(30f, 60f, 1.0f)
        }

        GLES31.glUniform2fv(blockSize, 1, FloatBuffer.wrap(size))
    }
}