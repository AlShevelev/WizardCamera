package com.shevelev.wizard_camera.main_activity.dto

data class FilterListItem(
    val displayData: FilterDisplayData,
    val favorite: FilterFavoriteType,
    val hasSettings: Boolean
)