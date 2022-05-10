package com.shevelev.wizard_camera.core.common_entities.filter_settings.gl

import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.enums.Size
import kotlinx.parcelize.Parcelize

@Parcelize
data class HexagonMosaicFilterSettings(
    override val code: GlFilterCode = GlFilterCode.HEXAGON_MOSAIC,
    val size: Size
): GlFilterSettings