package com.shevelev.wizard_camera.shared.filters_ui.filters_settings.system

import android.content.Context
import android.util.AttributeSet
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.bitmap.filters.effect.effects.SaturationEffect

class SystemFilterSettingsSaturation
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SystemFilterSettingsBase(context, attrs, defStyleAttr) {

    /**
     * Min value for a seek bar
     */
    override val displayFactorMin: Float = SaturationEffect.displayFactorMin

    /**
     * Max value for a seek bar
     */
    override val displayFactorMax: Float = SaturationEffect.displayFactorMax

    /**
     * Min value for an effect settings
     */
    override val effectFactorMin: Float = SaturationEffect.effectFactorMin

    /**
     * Max value for an effect settings
     */
    override val effectFactorMax: Float = SaturationEffect.effectFactorMax

    override val title: Int = R.string.filterSaturationSettings
}
