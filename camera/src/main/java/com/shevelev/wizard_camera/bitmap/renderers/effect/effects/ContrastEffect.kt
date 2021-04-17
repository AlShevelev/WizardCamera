package com.shevelev.wizard_camera.bitmap.renderers.effect.effects

import android.media.effect.EffectFactory
import android.util.Range

class ContrastEffect(
    sourceFactorStartValue: Float
) : EffectBase(
    Range(0f, 100f), sourceFactorStartValue, Range(0.5f, 1.5f), 1f) {

    override fun createEffect(factory: EffectFactory): android.media.effect.Effect =
        factory.createEffect(EffectFactory.EFFECT_CONTRAST).apply {
            setParameter("contrast", effectFactor)
        }
}