package com.shevelev.wizard_camera.core.camera_gl.impl.bitmap.filters.effect.effects

import android.media.effect.Effect
import android.media.effect.EffectFactory
import android.util.Range
import com.shevelev.wizard_camera.core.utils.ext.reduceToRange

internal abstract class EffectBase(
    private val displayFactorRange: Range<Float>,
    displayFactorStartValue: Float,
    private val effectFactorRange: Range<Float>,
    protected var effectFactor: Float
) {
    var displayFactor: Float = displayFactorStartValue
        set(value) {
            field = value
            this.effectFactor = value.reduceToRange(displayFactorRange, effectFactorRange)
        }


    abstract fun createEffect(factory: EffectFactory): Effect
}