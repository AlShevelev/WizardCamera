package com.shevelev.wizard_camera.bitmap.renderers.effect.effects

import android.media.effect.EffectFactory
import android.util.Range

class TemperatureEffect(
    sourceFactorStartValue: Float
) : EffectBase(
    Range(0f, 100f), sourceFactorStartValue, Range(0f, 1f), 0.5f) {

    override fun createEffect(factory: EffectFactory): android.media.effect.Effect =
        factory.createEffect(EffectFactory.EFFECT_TEMPERATURE).apply {
            setParameter("scale", effectFactor)
        }
}