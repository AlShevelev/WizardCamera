package com.shevelev.wizard_camera.core.camera_gl.impl.bitmap.filters.effect.effects

import android.media.effect.EffectFactory
import android.util.Range

internal class SaturationEffect(
    sourceFactorStartValue: Float
) : EffectBase(
    Range(displayFactorMin, displayFactorMax),
    sourceFactorStartValue,
    Range(effectFactorMin, effectFactorMax),
    effectStartValue) {

    override fun createEffect(factory: EffectFactory): android.media.effect.Effect =
        factory.createEffect(EffectFactory.EFFECT_SATURATE).apply {
            setParameter("scale", effectFactor)
        }

    companion object {
        const val displayFactorMin = 0f
        const val displayFactorMax = 100f

        const val effectFactorMin = -1f
        const val effectFactorMax = 1f

        const val effectStartValue = 0f
    }
}