package com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_settings

import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings

/**
 * A common interface for all filter setting widgets
 */
interface FilterSettingsWidget {
    val title: Int

    fun init(startSettings: GlFilterSettings)

    fun setOnSettingsChangeListener(listener: ((GlFilterSettings) -> Unit)?)
}