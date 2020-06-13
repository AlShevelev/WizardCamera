package com.shevelev.wizard_camera.common_entities.filter_settings

import com.shevelev.wizard_camera.common_entities.enums.FilterCode

data class NewspaperFilterSettings(
    override val code: FilterCode = FilterCode.NEWSPAPER,
    val isGrayscale: Boolean
): FilterSettings