package com.shevelev.wizard_camera.main_activity.view_model

import com.shevelev.wizard_camera.common_entities.enums.FilterCode

interface FilterEventsProcessor {
    fun onFavoriteFilterClick(code: FilterCode, isSelected: Boolean)
}