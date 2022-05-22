package com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel

import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode

interface FilterEventsProcessor {
    fun onFavoriteFilterClick(id: GlFilterCode, isSelected: Boolean) {}

    fun onSettingsClick(id: GlFilterCode)

    fun onFilterClick(id: GlFilterCode, listId: String)
}