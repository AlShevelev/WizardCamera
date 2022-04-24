package com.shevelev.wizard_camera.core.camera_gl.impl.bitmap.filters

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES31
import android.util.Size
import androidx.annotation.RawRes
import com.shevelev.wizard_camera.core.camera_gl.api.bitmap.filters.GlSurfaceShaderFilter
import com.shevelev.wizard_camera.core.camera_gl.api.shared.GLFilterSettings
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import javax.microedition.khronos.opengles.GL10

/**
 * Base class for all OpenGL renderers, based on fragment shaders
 */
internal class GlSurfaceShaderFilterImpl(
    context: Context,
    bitmap: Bitmap,
    @RawRes
    private val fragmentShaderResId: Int,
    screenSize: Size,
    private var settings: GLFilterSettings
) : GlSurfaceFilterBase(context, bitmap, fragmentShaderResId, screenSize),
    GlSurfaceShaderFilter {

    /**
     * A filter's code
     */
    override val code: GlFilterCode = settings.code

    override fun onDrawFrame(gl: GL10) {
        draw(textures[0])

        tryToGetFrameAsBitmap(gl)
    }

    override fun release() {
        GLES31.glDeleteTextures(1, textures, 0)
    }

    override fun setFragmentShaderParameters(texture: Int) {
        settings.passSettingsParams(program)
        super.setFragmentShaderParameters(texture)
    }

    override fun updateSettings(settings: GLFilterSettings) {
        this.settings = settings
        settings.passSettingsParams(program)
    }
}