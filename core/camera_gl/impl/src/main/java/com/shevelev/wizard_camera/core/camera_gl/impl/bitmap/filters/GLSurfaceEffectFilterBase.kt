package com.shevelev.wizard_camera.core.camera_gl.impl.bitmap.filters

import android.content.Context
import android.graphics.Bitmap
import android.media.effect.Effect
import android.media.effect.EffectContext
import android.media.effect.EffectFactory
import android.opengl.GLES31
import android.util.Size
import com.shevelev.wizard_camera.core.camera_gl.impl.R
import javax.microedition.khronos.opengles.GL10

/**
 * Base class for all OpenGL renderers, based on Android Media Effects
 */
abstract class GLSurfaceEffectFilterBase(
    context: Context,
    bitmap: Bitmap,
    screenSize: Size
) : GLSurfaceFilterBase(
    context,
    bitmap,
    R.raw.original,
    screenSize) {

    private var effectContext: EffectContext? = null
    private var effect: Effect? = null

    override fun onDrawFrame(gl: GL10) {
        effectContext = effectContext ?: EffectContext.createWithCurrentGlContext()
        effect?.release()

        effect = createEffect(effectContext!!.factory)
        effect!!.apply(textures[0], surfaceSize.width, surfaceSize.height, textures[1])

        draw(textures[1])
    }

    protected abstract fun createEffect(factory: EffectFactory): Effect

    override fun release() {
        GLES31.glDeleteTextures(2, textures, 0)
    }
}