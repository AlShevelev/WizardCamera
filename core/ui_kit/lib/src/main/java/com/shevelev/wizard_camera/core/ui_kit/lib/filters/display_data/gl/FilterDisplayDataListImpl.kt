package com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl

import com.shevelev.wizard_camera.core.ui_kit.lib.R
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.FilterDisplayData

class FilterDisplayDataListImpl : FilterDisplayDataList {
    private val items = mapOf(
        GlFilterCode.EDGE_DETECTION to FilterDisplayData(
            GlFilterCode.EDGE_DETECTION,
            R.drawable.img_filter_edge_detection,
            R.string.filterEdgeDetection
        ),

        GlFilterCode.PIXELIZE to FilterDisplayData(
            GlFilterCode.PIXELIZE,
            R.drawable.img_filter_pixelize,
            R.string.filterPixelize
        ),

        GlFilterCode.TRIANGLES_MOSAIC to FilterDisplayData(
            GlFilterCode.TRIANGLES_MOSAIC,
            R.drawable.img_filter_triangles_mosaic,
            R.string.filterTrianglesMosaic
        ),

        GlFilterCode.LEGOFIED to FilterDisplayData(
            GlFilterCode.LEGOFIED,
            R.drawable.img_filter_legofied,
            R.string.filterLegofied
        ),

        GlFilterCode.TILE_MOSAIC to FilterDisplayData(
            GlFilterCode.TILE_MOSAIC,
            R.drawable.img_filter_tile_mosaic,
            R.string.filterTileMosaic
        ),

        GlFilterCode.BLUE_ORANGE to FilterDisplayData(
            GlFilterCode.BLUE_ORANGE,
            R.drawable.img_filter_blue_orange,
            R.string.filterBlueorange
        ),

        GlFilterCode.BASIC_DEFORM to FilterDisplayData(
            GlFilterCode.BASIC_DEFORM,
            R.drawable.img_filter_basic_deform,
            R.string.filterBasicDeform
        ),

        GlFilterCode.CONTRAST to FilterDisplayData(
            GlFilterCode.CONTRAST,
            R.drawable.img_filter_contrast,
            R.string.filterContrast
        ),

        GlFilterCode.NOISE_WARP to FilterDisplayData(
            GlFilterCode.NOISE_WARP,
            R.drawable.img_filter_noise_warp,
            R.string.filterNoiseWarp
        ),

        GlFilterCode.REFRACTION to FilterDisplayData(
            GlFilterCode.REFRACTION,
            R.drawable.img_filter_refraction,
            R.string.filterRefraction
        ),

        GlFilterCode.MAPPING to FilterDisplayData(
            GlFilterCode.MAPPING,
            R.drawable.img_filter_mapping,
            R.string.filterMapping
        ),

        GlFilterCode.CROSSHATCH to FilterDisplayData(
            GlFilterCode.CROSSHATCH,
            R.drawable.img_filter_crosshatch,
            R.string.filterCrosshatch
        ),

        GlFilterCode.NEWSPAPER to FilterDisplayData(
            GlFilterCode.NEWSPAPER,
            R.drawable.img_filter_lichtenstein_esque,
            R.string.filterNewspaper
        ),

        GlFilterCode.ASCII_ART to FilterDisplayData(
            GlFilterCode.ASCII_ART,
            R.drawable.img_filter_ascii_art,
            R.string.filterAsciiArt
        ),

        GlFilterCode.MONEY to FilterDisplayData(
            GlFilterCode.MONEY,
            R.drawable.img_filter_money,
            R.string.filterMoney
        ),

        GlFilterCode.CRACKED to FilterDisplayData(
            GlFilterCode.CRACKED,
            R.drawable.img_filter_cracked,
            R.string.filterCracked
        ),

        GlFilterCode.POLYGONIZATION to FilterDisplayData(
            GlFilterCode.POLYGONIZATION,
            R.drawable.img_filter_polygonization,
            R.string.filterPolygonization
        ),

        GlFilterCode.BLACK_AND_WHITE to FilterDisplayData(
            GlFilterCode.BLACK_AND_WHITE,
            R.drawable.img_filter_black_and_white,
            R.string.filterBlackAndWhite
        ),

        GlFilterCode.GRAY to FilterDisplayData(
            GlFilterCode.GRAY,
            R.drawable.img_filter_gray,
            R.string.filterGray
        ),

        GlFilterCode.NEGATIVE to FilterDisplayData(
            GlFilterCode.NEGATIVE,
            R.drawable.img_filter_negative,
            R.string.filterNegative
        ),

        GlFilterCode.NOSTALGIA to FilterDisplayData(
            GlFilterCode.NOSTALGIA,
            R.drawable.img_filter_nostalgia,
            R.string.filterNostalgia
        ),

        GlFilterCode.CASTING to FilterDisplayData(
            GlFilterCode.CASTING,
            R.drawable.img_filter_casting,
            R.string.filterCasting
        ),

        GlFilterCode.RELIEF to FilterDisplayData(
            GlFilterCode.RELIEF,
            R.drawable.img_filter_relief,
            R.string.filterRelief
        ),

        GlFilterCode.SWIRL to FilterDisplayData(
            GlFilterCode.SWIRL,
            R.drawable.img_filter_swirl,
            R.string.filterSwirl
        ),

        GlFilterCode.HEXAGON_MOSAIC to FilterDisplayData(
            GlFilterCode.HEXAGON_MOSAIC,
            R.drawable.img_filter_hexagon_mosaic,
            R.string.filterHexagonMosaic
        ),

        GlFilterCode.MIRROR to FilterDisplayData(
            GlFilterCode.MIRROR,
            R.drawable.img_filter_mirror,
            R.string.filterMirror
        ),

        GlFilterCode.TRIPLE to FilterDisplayData(
            GlFilterCode.TRIPLE,
            R.drawable.img_filter_triple,
            R.string.filterTriple
        ),

        GlFilterCode.CARTOON to FilterDisplayData(
            GlFilterCode.CARTOON,
            R.drawable.img_filter_cartoon,
            R.string.filterCartoon
        ),

        GlFilterCode.WATER_REFLECTION to FilterDisplayData(
            GlFilterCode.WATER_REFLECTION,
            R.drawable.img_filter_water_reflection,
            R.string.filterWaterReflection
        )
    )

    override operator fun get(code: GlFilterCode) = items[code]!!

    override fun exists(code: GlFilterCode) = items.containsKey(code)
}