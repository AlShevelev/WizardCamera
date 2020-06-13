package com.shevelev.wizard_camera.common_entities.filter_settings

import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.common_entities.enums.MappingFilterTexture

data class MappingFilterSettings(
    override val code: FilterCode = FilterCode.MAPPING,
    val texture: MappingFilterTexture,

    // From 5(included) to 20(included)
    val mixFactor: Int
): FilterSettings