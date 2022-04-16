package com.shevelev.wizard_camera.core.camera_gl.bitmap.filters.effect.effects

import android.media.effect.EffectFactory
import android.util.Range

class TemperatureEffect(
    sourceFactorStartValue: Float
) : EffectBase(
    Range(displayFactorMin, displayFactorMax),
    sourceFactorStartValue,
    Range(effectFactorMin, effectFactorMax),
    effectStartValue) {

    override fun createEffect(factory: EffectFactory): android.media.effect.Effect =
        factory.createEffect(EffectFactory.EFFECT_TEMPERATURE).apply {
            setParameter("scale", effectFactor)
        }

    companion object {
        const val displayFactorMin = 0f
        const val displayFactorMax = 100f

        const val effectFactorMin = 0f
        const val effectFactorMax = 1f

        const val effectStartValue = 0.5f
    }
}