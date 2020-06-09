package com.shevelev.wizard_camera.common_entities.filter_settings

import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.common_entities.enums.Size

data class HexagonMosaicFilterSettings(
    override val code: FilterCode = FilterCode.HEXAGON_MOSAIC,
    val size: Size
): FilterSettings