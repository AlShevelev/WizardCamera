package com.shevelev.wizard_camera.bitmap.filters

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES31
import android.util.Size
import androidx.annotation.RawRes
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import javax.microedition.khronos.opengles.GL10

/**
 * Base class for all OpenGL renderers, based on fragment shaders
 */
abstract class GLSurfaceShaderFilterBase(
    context: Context,
    bitmap: Bitmap,
    @RawRes
    private val fragmentShaderResId: Int,
    screenSize: Size,
    private var settings: FilterSettings
) : GLSurfaceFilterBase(context, bitmap, fragmentShaderResId, screenSize) {

    override fun onDrawFrame(gl: GL10) {
        draw(textures[0])
    }

    override fun release() {
        GLES31.glDeleteTextures(1, textures, 0)
    }

    override fun setFragmentShaderParameters(texture: Int) {
        super.setFragmentShaderParameters(texture)
        useSettings(settings)
    }

    fun updateSettings(settings: FilterSettings) {
        this.settings = settings
        useSettings(settings)
        surface.requestRender()
    }

    open fun useSettings(settings: FilterSettings) {
        // do nothing
    }
}