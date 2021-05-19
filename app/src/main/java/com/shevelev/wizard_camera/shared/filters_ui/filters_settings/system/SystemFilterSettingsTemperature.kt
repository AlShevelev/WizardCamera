package com.shevelev.wizard_camera.shared.filters_ui.filters_settings.system

import android.content.Context
import android.util.AttributeSet
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.bitmap.filters.effect.effects.TemperatureEffect

class SystemFilterSettingsTemperature
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SystemFilterSettingsBase(context, attrs, defStyleAttr) {

    /**
     * Min value for a seek bar
     */
    override val displayFactorMin: Float = TemperatureEffect.displayFactorMin

    /**
     * Max value for a seek bar
     */
    override val displayFactorMax: Float = TemperatureEffect.displayFactorMax

    /**
     * Min value for an effect settings
     */
    override val effectFactorMin: Float = TemperatureEffect.effectFactorMin

    /**
     * Max value for an effect settings
     */
    override val effectFactorMax: Float = TemperatureEffect.effectFactorMax

    override val title: Int = R.string.filterTemperatureSettings
}