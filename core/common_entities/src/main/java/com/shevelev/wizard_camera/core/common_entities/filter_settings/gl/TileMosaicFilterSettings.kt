package com.shevelev.wizard_camera.core.common_entities.filter_settings.gl

import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import kotlinx.parcelize.Parcelize

@Parcelize
data class TileMosaicFilterSettings(
    override val code: GlFilterCode = GlFilterCode.TILE_MOSAIC,

    /**
     * from 40(included) to 100(included)
     */
    val tileSize: Int,

    /**
     * from 1(included) to 5(included)
     */
    val borderSize: Int
): GlFilterSettings