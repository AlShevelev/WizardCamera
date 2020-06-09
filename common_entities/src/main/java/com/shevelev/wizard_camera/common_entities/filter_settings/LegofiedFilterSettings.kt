package com.shevelev.wizard_camera.common_entities.filter_settings

import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.common_entities.enums.Size

data class LegofiedFilterSettings(
    override val code: FilterCode = FilterCode.LEGOFIED,
    val size: Size
): FilterSettings