package com.shevelev.wizard_camera.core.camera_gl.shared.filters_ui.filters_carousel

import com.shevelev.wizard_camera.core.camera_gl.shared.filters_ui.display_data.FilterDisplayData

data class FilterListItem(
    val displayData: FilterDisplayData,
    val favorite: FilterFavoriteType,
    val hasSettings: Boolean,
    val isSelected: Boolean
)