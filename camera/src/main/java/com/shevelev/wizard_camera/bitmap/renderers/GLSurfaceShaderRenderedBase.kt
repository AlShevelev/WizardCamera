package com.shevelev.wizard_camera.bitmap.renderers

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES31
import androidx.annotation.RawRes
import javax.microedition.khronos.opengles.GL10

/**
 * Base class for all OpenGL renderers, based on fragment shaders
 */
abstract class GLSurfaceShaderRenderedBase(
    context: Context,
    bitmap: Bitmap,
    @RawRes
    private val fragmentShaderResId: Int
) : GLSurfaceRenderedBase(context, bitmap, fragmentShaderResId) {

    override fun onDrawFrame(gl: GL10) {
        draw(textures[0])
    }

    override fun release() {
        GLES31.glDeleteTextures(1, textures, 0)
    }
}