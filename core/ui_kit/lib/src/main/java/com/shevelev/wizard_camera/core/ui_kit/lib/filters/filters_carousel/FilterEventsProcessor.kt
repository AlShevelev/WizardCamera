package com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel

import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode

interface FilterEventsProcessor {
    fun onFavoriteFilterClick(code: GlFilterCode, isSelected: Boolean)

    fun onSettingsClick(code: GlFilterCode)

    fun onFilterClick(code: GlFilterCode, listId: String)
}