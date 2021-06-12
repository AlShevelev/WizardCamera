package com.shevelev.wizard_camera.shared.filters_ui.filters_carousel

import com.shevelev.wizard_camera.shared.filters_ui.display_data.FilterDisplayData

data class FilterListItem(
    val displayData: FilterDisplayData,
    val favorite: FilterFavoriteType,
    val hasSettings: Boolean
)