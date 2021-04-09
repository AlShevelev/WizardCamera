package com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto

data class FilterListItem(
    val displayData: FilterDisplayData,
    val favorite: FilterFavoriteType,
    val hasSettings: Boolean
)