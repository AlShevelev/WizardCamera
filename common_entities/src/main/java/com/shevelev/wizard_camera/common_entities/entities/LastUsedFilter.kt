package com.shevelev.wizard_camera.common_entities.entities

import com.shevelev.wizard_camera.common_entities.enums.FilterCode

data class LastUsedFilter(
    val code: FilterCode,
    val isFavorite: Boolean
)