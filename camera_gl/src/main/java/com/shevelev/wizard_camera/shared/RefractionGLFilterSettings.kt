package com.shevelev.wizard_camera.shared

import android.content.Context
import android.opengl.GLES31
import com.shevelev.wizard_camera.camera.R
import com.shevelev.wizard_camera.camera.renderer.utils.TextureUtils
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.EmptyFilterSettings

/**
 * A class for passing parameters into the Refraction OGL filter
 */
class RefractionGLFilterSettings(
    private val context: Context,
    settings: EmptyFilterSettings
) : GLFilterSettingsBase<EmptyFilterSettings>(settings) {
    /**
     * Passes params
     * @param program OGL program
     */
    override fun passSettingsParams(program: Int) {
        val textureId = TextureUtils.loadTexture(context, R.raw.tex11, IntArray(2))

        val sTextureLocation = GLES31.glGetUniformLocation(program, "iChannel1")
        GLES31.glActiveTexture(GLES31.GL_TEXTURE0 + 1)
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureId)
        GLES31.glUniform1i(sTextureLocation, 1)
    }
}