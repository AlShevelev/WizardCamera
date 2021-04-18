package com.shevelev.wizard_camera.bitmap.renderers

import android.content.Context
import android.graphics.Bitmap
import android.media.effect.Effect
import android.media.effect.EffectContext
import android.media.effect.EffectFactory
import android.opengl.GLES31
import com.shevelev.wizard_camera.camera.R
import javax.microedition.khronos.opengles.GL10

/**
 * Base class for all OpenGL renderers, based on Android Media Effects
 */
abstract class GLSurfaceEffectRenderedBase(
    context: Context,
    bitmap: Bitmap
) : GLSurfaceRenderedBase(context, bitmap, R.raw.original) {

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