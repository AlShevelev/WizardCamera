package com.shevelev.wizard_camera.core.camera_gl.shared.filters_ui.filters_carousel

import com.shevelev.wizard_camera.core.camera_gl.shared.filters_ui.display_data.FilterDisplayId

interface FilterEventsProcessor {
    fun onFavoriteFilterClick(id: FilterDisplayId, isSelected: Boolean) {}

    fun onSettingsClick(id: FilterDisplayId)
}