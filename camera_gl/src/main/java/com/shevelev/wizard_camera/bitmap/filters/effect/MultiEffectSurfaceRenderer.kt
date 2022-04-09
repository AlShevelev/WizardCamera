package com.shevelev.wizard_camera.bitmap.filters.effect

import android.content.Context
import android.graphics.Bitmap
import android.media.effect.Effect
import android.media.effect.EffectFactory
import android.util.Size
import com.shevelev.wizard_camera.bitmap.filters.GLSurfaceEffectFilterBase
import com.shevelev.wizard_camera.bitmap.filters.effect.effects.EffectBase

class MultiEffectSurfaceRenderer(
    context: Context,
    bitmap: Bitmap,
    private val effects: List<EffectBase>,
    private var effectIndex: Int,
    private val screenSize: Size
): GLSurfaceEffectFilterBase(context, bitmap, screenSize) {

    val sourceFactor: Float
        get() = effects[effectIndex].displayFactor

    override fun createEffect(factory: EffectFactory): Effect = effects[effectIndex].createEffect(factory)

    fun update(factor: Float) {
        effects[effectIndex].displayFactor = factor
        surface.requestRender()
    }

    fun switch(index: Int) {
        effectIndex = index
        surface.requestRender()
    }

    fun clone(context: Context, bitmap: Bitmap, effectIndex: Int) =
        MultiEffectSurfaceRenderer(context, bitmap, effects, effectIndex, screenSize)

    fun getSourceFactor(index: Int) = effects[index].displayFactor
}
