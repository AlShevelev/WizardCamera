package com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel

import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.FilterDisplayData
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel.FilterFavoriteType

/**
 * @property listId A unique type of the list that contains this item
 */
data class FilterListItem(
    val listId: String,
    val displayData: FilterDisplayData,
    val favorite: FilterFavoriteType,
    val hasSettings: Boolean,
    val isSelected: Boolean
)