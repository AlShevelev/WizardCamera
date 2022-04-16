package com.shevelev.wizard_camera.core.camera_gl.bitmap.filters.effect

import android.content.Context
import android.graphics.Bitmap
import android.media.effect.Effect
import android.media.effect.EffectFactory
import android.util.Size
import com.shevelev.wizard_camera.core.camera_gl.bitmap.filters.GLSurfaceEffectFilterBase
import com.shevelev.wizard_camera.core.camera_gl.bitmap.filters.effect.effects.EffectBase

class OneEffectSurfaceRenderer(
    context: Context,
    bitmap: Bitmap,
    private val effect: EffectBase,
    screenSize: Size
): GLSurfaceEffectFilterBase(context, bitmap, screenSize) {

    val sourceFactor: Float
        get() = effect.displayFactor

    override fun createEffect(factory: EffectFactory): Effect = effect.createEffect(factory)

    fun update(factor: Float) {
        effect.displayFactor = factor
        surface.requestRender()
    }
}
