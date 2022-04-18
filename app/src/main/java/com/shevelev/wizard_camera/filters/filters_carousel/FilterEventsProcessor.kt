package com.shevelev.wizard_camera.filters.filters_carousel

import com.shevelev.wizard_camera.filters.display_data.FilterDisplayId

interface FilterEventsProcessor {
    fun onFavoriteFilterClick(id: FilterDisplayId, isSelected: Boolean) {}

    fun onSettingsClick(id: FilterDisplayId)
}