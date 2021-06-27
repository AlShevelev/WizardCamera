package com.shevelev.wizard_camera.bitmap.filters

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES31
import android.util.Size
import androidx.annotation.RawRes
import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.shared.GLFilterSettings
import javax.microedition.khronos.opengles.GL10

/**
 * Base class for all OpenGL renderers, based on fragment shaders
 */
class GLSurfaceShaderFilter(
    context: Context,
    bitmap: Bitmap,
    @RawRes
    private val fragmentShaderResId: Int,
    screenSize: Size,
    private var settings: GLFilterSettings
) : GLSurfaceFilterBase(context, bitmap, fragmentShaderResId, screenSize) {

    /**
     * A filter's code
     */
    val code: GlFilterCode = settings.code

    override fun onDrawFrame(gl: GL10) {
        draw(textures[0])
    }

    override fun release() {
        GLES31.glDeleteTextures(1, textures, 0)
    }

    override fun setFragmentShaderParameters(texture: Int) {
        settings.passSettingsParams(program)
        super.setFragmentShaderParameters(texture)
    }

    fun updateSettings(settings: GLFilterSettings) {
        this.settings = settings
        settings.passSettingsParams(program)
    }
}