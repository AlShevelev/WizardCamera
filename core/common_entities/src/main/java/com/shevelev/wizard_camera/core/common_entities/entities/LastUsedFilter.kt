package com.shevelev.wizard_camera.core.common_entities.entities

import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode

data class LastUsedFilter(
    val code: GlFilterCode,
    val group: FiltersGroup
)