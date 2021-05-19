package com.shevelev.wizard_camera.common_entities.entities

import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode

data class LastUsedFilter(
    val code: GlFilterCode,
    val isFavorite: Boolean
)