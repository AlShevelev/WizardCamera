package com.shevelev.wizard_camera.camera.filter

import android.content.Context
import android.opengl.GLES31
import com.shevelev.wizard_camera.camera.R
import com.shevelev.wizard_camera.camera.utils.TextureUtils
import com.shevelev.wizard_camera.common_entities.enums.MappingFilterTexture
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.MappingFilterSettings

class MappingCameraFilter(
    private val context: Context
) : CameraFilter(context, R.raw.mapping) {

    /**
     * Pass filter-specific arguments
     */
    override fun passSettingsParams(program: Int, settings: FilterSettings) {
        settings as MappingFilterSettings

        val textureId = loadTexture(settings.texture)

        val sTextureLocation = GLES31.glGetUniformLocation(program, "iChannel1")
        GLES31.glActiveTexture(GLES31.GL_TEXTURE0 + 1)
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureId)
        GLES31.glUniform1i(sTextureLocation, 1)

        val mixFactor = GLES31.glGetUniformLocation(program, "mixFactor")
        GLES31.glUniform1f(mixFactor, settings.mixFactor.toFloat() / 10f)
    }

    private fun loadTexture(texture: MappingFilterTexture): Int {
        val textureRes = when(texture) {
            MappingFilterTexture.TEXTURE_0 -> R.raw.mapping_tex0
            MappingFilterTexture.TEXTURE_1 -> R.raw.mapping_tex1
            MappingFilterTexture.TEXTURE_2 -> R.raw.mapping_tex2
            MappingFilterTexture.TEXTURE_3 -> R.raw.mapping_tex3
            MappingFilterTexture.TEXTURE_4 -> R.raw.mapping_tex4
            MappingFilterTexture.TEXTURE_5 -> R.raw.mapping_tex5
            MappingFilterTexture.TEXTURE_6 -> R.raw.mapping_tex6
            MappingFilterTexture.TEXTURE_7 -> R.raw.mapping_tex7
            MappingFilterTexture.TEXTURE_8 -> R.raw.mapping_tex8
            MappingFilterTexture.TEXTURE_9 -> R.raw.mapping_tex9
            MappingFilterTexture.TEXTURE_10 -> R.raw.mapping_tex10
            MappingFilterTexture.TEXTURE_11 -> R.raw.mapping_tex11
            MappingFilterTexture.TEXTURE_12 -> R.raw.mapping_tex12
            MappingFilterTexture.TEXTURE_13 -> R.raw.mapping_tex13
            MappingFilterTexture.TEXTURE_14 -> R.raw.mapping_tex14
        }

        return TextureUtils.loadTexture(context, textureRes, IntArray(2))
    }
}