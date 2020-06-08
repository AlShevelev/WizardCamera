package com.shevelev.wizard_camera.common_entities.filter_settings

import com.shevelev.wizard_camera.common_entities.enums.FilterCode

data class BlackAndWhiteFilterSettings(
    override val code: FilterCode = FilterCode.BLACK_AND_WHITE,
    val isInverted: Boolean
): FilterSettings