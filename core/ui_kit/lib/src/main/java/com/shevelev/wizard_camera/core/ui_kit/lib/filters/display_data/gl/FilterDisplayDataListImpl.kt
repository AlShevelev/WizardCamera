package com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl

import com.shevelev.wizard_camera.core.ui_kit.lib.R
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.FilterDisplayData

class FilterDisplayDataListImpl : FilterDisplayDataList {
    private val items = listOf(
        FilterDisplayData(
            GlFilterCode.EDGE_DETECTION,
            R.drawable.img_filter_edge_detection,
            R.string.filterEdgeDetection
        ),

        FilterDisplayData(
            GlFilterCode.PIXELIZE,
            R.drawable.img_filter_pixelize,
            R.string.filterPixelize
        ),

        FilterDisplayData(
            GlFilterCode.TRIANGLES_MOSAIC,
            R.drawable.img_filter_triangles_mosaic,
            R.string.filterTrianglesMosaic
        ),

        FilterDisplayData(
            GlFilterCode.LEGOFIED,
            R.drawable.img_filter_legofied,
            R.string.filterLegofied
        ),

        FilterDisplayData(
            GlFilterCode.TILE_MOSAIC,
            R.drawable.img_filter_tile_mosaic,
            R.string.filterTileMosaic
        ),

        FilterDisplayData(
            GlFilterCode.BLUE_ORANGE,
            R.drawable.img_filter_blue_orange,
            R.string.filterBlueorange
        ),

        FilterDisplayData(
            GlFilterCode.BASIC_DEFORM,
            R.drawable.img_filter_basic_deform,
            R.string.filterBasicDeform
        ),

        FilterDisplayData(
            GlFilterCode.CONTRAST,
            R.drawable.img_filter_contrast,
            R.string.filterContrast
        ),

        FilterDisplayData(
            GlFilterCode.NOISE_WARP,
            R.drawable.img_filter_noise_warp,
            R.string.filterNoiseWarp
        ),

        FilterDisplayData(
            GlFilterCode.REFRACTION,
            R.drawable.img_filter_refraction,
            R.string.filterRefraction
        ),

        FilterDisplayData(
            GlFilterCode.MAPPING,
            R.drawable.img_filter_mapping,
            R.string.filterMapping
        ),

        FilterDisplayData(
            GlFilterCode.CROSSHATCH,
            R.drawable.img_filter_crosshatch,
            R.string.filterCrosshatch
        ),

        FilterDisplayData(
            GlFilterCode.NEWSPAPER,
            R.drawable.img_filter_lichtenstein_esque,
            R.string.filterNewspaper
        ),

        FilterDisplayData(
            GlFilterCode.ASCII_ART,
            R.drawable.img_filter_ascii_art,
            R.string.filterAsciiArt
        ),

        FilterDisplayData(
            GlFilterCode.MONEY,
            R.drawable.img_filter_money,
            R.string.filterMoney
        ),

        FilterDisplayData(
            GlFilterCode.CRACKED,
            R.drawable.img_filter_cracked,
            R.string.filterCracked
        ),

        FilterDisplayData(
            GlFilterCode.POLYGONIZATION,
            R.drawable.img_filter_polygonization,
            R.string.filterPolygonization
        ),

        FilterDisplayData(
            GlFilterCode.BLACK_AND_WHITE,
            R.drawable.img_filter_black_and_white,
            R.string.filterBlackAndWhite
        ),

        FilterDisplayData(
            GlFilterCode.GRAY,
            R.drawable.img_filter_gray,
            R.string.filterGray
        ),

        FilterDisplayData(
            GlFilterCode.NEGATIVE,
            R.drawable.img_filter_negative,
            R.string.filterNegative
        ),

        FilterDisplayData(
            GlFilterCode.NOSTALGIA,
            R.drawable.img_filter_nostalgia,
            R.string.filterNostalgia
        ),

        FilterDisplayData(
            GlFilterCode.CASTING,
            R.drawable.img_filter_casting,
            R.string.filterCasting
        ),

        FilterDisplayData(
            GlFilterCode.RELIEF,
            R.drawable.img_filter_relief,
            R.string.filterRelief
        ),

        FilterDisplayData(
            GlFilterCode.SWIRL,
            R.drawable.img_filter_swirl,
            R.string.filterSwirl
        ),

        FilterDisplayData(
            GlFilterCode.HEXAGON_MOSAIC,
            R.drawable.img_filter_hexagon_mosaic,
            R.string.filterHexagonMosaic
        ),

        FilterDisplayData(
            GlFilterCode.MIRROR,
            R.drawable.img_filter_mirror,
            R.string.filterMirror
        ),

        FilterDisplayData(
            GlFilterCode.TRIPLE,
            R.drawable.img_filter_triple,
            R.string.filterTriple
        ),

        FilterDisplayData(
            GlFilterCode.CARTOON,
            R.drawable.img_filter_cartoon,
            R.string.filterCartoon
        ),

        FilterDisplayData(
            GlFilterCode.WATER_REFLECTION,
            R.drawable.img_filter_water_reflection,
            R.string.filterWaterReflection
        )
    )

    override operator fun get(index: Int) = items[index]

    override operator fun get(code: GlFilterCode) = items.single { it.code == code }

    override fun exists(code: GlFilterCode) = items.any { it.code == code }

    override fun <T>map(mapActon: (FilterDisplayData) -> T): List<T> = items.map { mapActon(it) }

    override fun getIndex(code: GlFilterCode): Int = items.indexOfFirst { it.code == code }
}