package com.shevelev.wizard_camera.filters.filters_carousel

import com.shevelev.wizard_camera.filters.display_data.FilterDisplayData

data class FilterListItem(
    val displayData: FilterDisplayData,
    val favorite: FilterFavoriteType,
    val hasSettings: Boolean,
    val isSelected: Boolean
)