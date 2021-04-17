package com.shevelev.wizard_camera.bitmap.renderers.effect

import android.content.Context
import android.graphics.Bitmap
import android.media.effect.Effect
import android.media.effect.EffectFactory
import com.shevelev.wizard_camera.bitmap.renderers.GLSurfaceEffectRenderedBase
import com.shevelev.wizard_camera.bitmap.renderers.effect.effects.EffectBase

class OneEffectSurfaceRenderer(
    context: Context,
    bitmap: Bitmap,
    private val effect: EffectBase
): GLSurfaceEffectRenderedBase(context, bitmap) {

    val sourceFactor: Float
        get() = effect.sourceFactor

    override fun createEffect(factory: EffectFactory): Effect = effect.createEffect(factory)

    fun update(factor: Float) {
        effect.sourceFactor = factor
        surface.requestRender()
    }
}
