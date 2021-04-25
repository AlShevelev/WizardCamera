package com.shevelev.wizard_camera.bitmap.filters.effect.effects

import android.media.effect.EffectFactory
import android.util.Range

class SaturationEffect(
    sourceFactorStartValue: Float
) : EffectBase(
    Range(0f, 100f), sourceFactorStartValue, Range(-1f, 1f), 0f) {

    override fun createEffect(factory: EffectFactory): android.media.effect.Effect =
        factory.createEffect(EffectFactory.EFFECT_SATURATE).apply {
            setParameter("scale", effectFactor)
        }
}