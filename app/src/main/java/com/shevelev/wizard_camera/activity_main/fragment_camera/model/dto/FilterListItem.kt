package com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto

import com.shevelev.wizard_camera.shared.filters_ui.dto.FilterDisplayData

data class FilterListItem(
    val displayData: FilterDisplayData,
    val favorite: FilterFavoriteType,
    val hasSettings: Boolean
)