package com.shevelev.wizard_camera.shared.filters_ui.filters_settings

import com.shevelev.wizard_camera.core.common_entities.filter_settings.FilterSettings

/**
 * A common interface for all filter setting widgets
 */
interface FilterSettingsWidget<T: FilterSettings<*>> {
    val title: Int

    fun init(startSettings: T)

    fun setOnSettingsChangeListener(listener: ((T) -> Unit)?)
}