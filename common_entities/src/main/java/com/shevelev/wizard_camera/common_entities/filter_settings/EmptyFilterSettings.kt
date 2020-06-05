package com.shevelev.wizard_camera.common_entities.filter_settings

import com.shevelev.wizard_camera.common_entities.enums.FilterCode

data class EmptyFilterSettings(
    override val code: FilterCode
): FilterSettings