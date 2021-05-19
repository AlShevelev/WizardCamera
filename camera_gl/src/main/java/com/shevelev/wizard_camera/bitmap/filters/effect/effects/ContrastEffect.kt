package com.shevelev.wizard_camera.bitmap.filters.effect.effects

import android.media.effect.EffectFactory
import android.util.Range

class ContrastEffect(
    sourceFactorStartValue: Float
) : EffectBase(
    Range(displayFactorMin, displayFactorMax),
    sourceFactorStartValue,
    Range(effectFactorMin, effectFactorMax),
    effectStartValue) {

    override fun createEffect(factory: EffectFactory): android.media.effect.Effect =
        factory.createEffect(EffectFactory.EFFECT_CONTRAST).apply {
            setParameter("contrast", effectFactor)
        }

    companion object {
        const val displayFactorMin = 0f
        const val displayFactorMax = 100f

        const val effectFactorMin = 0.5f
        const val effectFactorMax = 1.5f

        const val effectStartValue = 1f
    }
}