package com.shevelev.wizard_camera.shared.filters_ui.filters_settings.system.host

import android.content.Context
import android.util.AttributeSet
import com.shevelev.wizard_camera.shared.filters_ui.filters_settings.FilterSettingsHostBase
import com.shevelev.wizard_camera.shared.filters_ui.filters_settings.FilterSettingsWidget
import com.shevelev.wizard_camera.shared.filters_ui.filters_settings.system.SystemFilterSettingsBrightness
import com.shevelev.wizard_camera.shared.filters_ui.filters_settings.system.SystemFilterSettingsContrast
import com.shevelev.wizard_camera.shared.filters_ui.filters_settings.system.SystemFilterSettingsSaturation
import com.shevelev.wizard_camera.shared.filters_ui.filters_settings.system.SystemFilterSettingsTemperature
import com.shevelev.wizard_camera.common_entities.enums.SystemFilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.system.SystemFilterSettings

class SystemFilterSettingsHost
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FilterSettingsHostBase<SystemFilterSettings>(context, attrs, defStyleAttr)  {

    override fun createWidget(settings: SystemFilterSettings): FilterSettingsWidget<SystemFilterSettings> =
        when(settings.code) {
            SystemFilterCode.BRIGHTNESS -> SystemFilterSettingsBrightness(context)
            SystemFilterCode.CONTRAST -> SystemFilterSettingsContrast(context)
            SystemFilterCode.SATURATION -> SystemFilterSettingsSaturation(context)
            SystemFilterCode.TEMPERATURE -> SystemFilterSettingsTemperature(context)
            else -> throw UnsupportedOperationException("This code is not supported: ${settings.code}")
        }
}