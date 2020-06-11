package com.shevelev.wizard_camera.common_entities.filter_settings

import com.shevelev.wizard_camera.common_entities.enums.FilterCode

data class TripleFilterSettings(
    override val code: FilterCode = FilterCode.TRIPLE,
    val isHorizontal: Boolean
): FilterSettings