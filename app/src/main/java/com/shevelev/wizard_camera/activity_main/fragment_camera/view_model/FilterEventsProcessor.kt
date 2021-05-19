package com.shevelev.wizard_camera.activity_main.fragment_camera.view_model

import com.shevelev.wizard_camera.shared.filters_ui.dto.FilterDisplayId

interface FilterEventsProcessor {
    fun onFavoriteFilterClick(id: FilterDisplayId, isSelected: Boolean)

    fun onSettingsClick(id: FilterDisplayId)
}