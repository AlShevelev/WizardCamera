package com.shevelev.wizard_camera.camera.filters

import android.content.Context
import android.opengl.GLES31
import com.shevelev.wizard_camera.camera.R
import com.shevelev.wizard_camera.camera.camera_renderer.utils.TextureUtils
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings

class RefractionCameraFilter(
    private val context: Context
) : CameraFilter(context, R.raw.refraction) {
    /**
     * Pass filter-specific arguments
     */
    override fun passSettingsParams(program: Int, settings: FilterSettings) {
        val textureId = TextureUtils.loadTexture(context, R.raw.tex11, IntArray(2))

        val sTextureLocation = GLES31.glGetUniformLocation(program, "iChannel1")
        GLES31.glActiveTexture(GLES31.GL_TEXTURE0 + 1)
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureId)
        GLES31.glUniform1i(sTextureLocation, 1)
    }
}

