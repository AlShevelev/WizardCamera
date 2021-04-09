package com.shevelev.wizard_camera.activity_main.fragment_camera.view_model

import com.shevelev.wizard_camera.common_entities.enums.FilterCode

interface FilterEventsProcessor {
    fun onFavoriteFilterClick(code: FilterCode, isSelected: Boolean)

    fun onSettingsClick(code: FilterCode)
}