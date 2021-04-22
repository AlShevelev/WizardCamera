package com.shevelev.wizard_camera.bitmap.filters.effect

import android.content.Context
import android.graphics.Bitmap
import android.media.effect.Effect
import android.media.effect.EffectFactory
import com.shevelev.wizard_camera.bitmap.filters.GLSurfaceEffectFilterBase
import com.shevelev.wizard_camera.bitmap.filters.effect.effects.EffectBase

class MultiEffectSurfaceRenderer(
    context: Context,
    bitmap: Bitmap,
    private val effects: List<EffectBase>,
    private var effectIndex: Int
): GLSurfaceEffectFilterBase(context, bitmap) {

    val sourceFactor: Float
        get() = effects[effectIndex].sourceFactor

    override fun createEffect(factory: EffectFactory): Effect = effects[effectIndex].createEffect(factory)

    fun update(factor: Float) {
        effects[effectIndex].sourceFactor = factor
        surface.requestRender()
    }

    fun switch(index: Int) {
        effectIndex = index
        surface.requestRender()
    }

    fun clone(context: Context, bitmap: Bitmap, effectIndex: Int) =
        MultiEffectSurfaceRenderer(context, bitmap, effects, effectIndex)

    fun getSourceFactor(index: Int) = effects[index].sourceFactor
}
