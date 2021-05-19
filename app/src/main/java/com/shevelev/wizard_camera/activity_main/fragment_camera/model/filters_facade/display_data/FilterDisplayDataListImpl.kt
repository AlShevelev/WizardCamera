package com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.display_data

import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.shared.filters_ui.dto.FilterDisplayData
import com.shevelev.wizard_camera.shared.filters_ui.dto.FilterDisplayId
import javax.inject.Inject

class FilterDisplayDataListImpl
@Inject
constructor() : FilterDisplayDataList {
    private val items = listOf(
        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.EDGE_DETECTION),
            R.drawable.img_filter_edge_detection,
            R.string.filterEdgeDetection
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.PIXELIZE),
            R.drawable.img_filter_pixelize,
            R.string.filterPixelize
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.TRIANGLES_MOSAIC),
            R.drawable.img_filter_triangles_mosaic,
            R.string.filterTrianglesMosaic
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.LEGOFIED),
            R.drawable.img_filter_legofied,
            R.string.filterLegofied
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.TILE_MOSAIC),
            R.drawable.img_filter_tile_mosaic,
            R.string.filterTileMosaic
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.BLUE_ORANGE),
            R.drawable.img_filter_blue_orange,
            R.string.filterBlueorange
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.BASIC_DEFORM),
            R.drawable.img_filter_basic_deform,
            R.string.filterBasicDeform
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.CONTRAST),
            R.drawable.img_filter_contrast,
            R.string.filterContrast
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.NOISE_WARP),
            R.drawable.img_filter_noise_warp,
            R.string.filterNoiseWarp
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.REFRACTION),
            R.drawable.img_filter_refraction,
            R.string.filterRefraction
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.MAPPING),
            R.drawable.img_filter_mapping,
            R.string.filterMapping
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.CROSSHATCH),
            R.drawable.img_filter_crosshatch,
            R.string.filterCrosshatch
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.NEWSPAPER),
            R.drawable.img_filter_lichtenstein_esque,
            R.string.filterNewspaper
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.ASCII_ART),
            R.drawable.img_filter_ascii_art,
            R.string.filterAsciiArt
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.MONEY),
            R.drawable.img_filter_money,
            R.string.filterMoney
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.CRACKED),
            R.drawable.img_filter_cracked,
            R.string.filterCracked
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.POLYGONIZATION),
            R.drawable.img_filter_polygonization,
            R.string.filterPolygonization
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.BLACK_AND_WHITE),
            R.drawable.img_filter_black_and_white,
            R.string.filterBlackAndWhite
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.GRAY),
            R.drawable.img_filter_gray,
            R.string.filterGray
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.NEGATIVE),
            R.drawable.img_filter_negative,
            R.string.filterNegative
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.NOSTALGIA),
            R.drawable.img_filter_nostalgia,
            R.string.filterNostalgia
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.CASTING),
            R.drawable.img_filter_casting,
            R.string.filterCasting
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.RELIEF),
            R.drawable.img_filter_relief,
            R.string.filterRelief
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.SWIRL),
            R.drawable.img_filter_swirl,
            R.string.filterSwirl
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.HEXAGON_MOSAIC),
            R.drawable.img_filter_hexagon_mosaic,
            R.string.filterHexagonMosaic
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.MIRROR),
            R.drawable.img_filter_mirror,
            R.string.filterMirror
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.TRIPLE),
            R.drawable.img_filter_triple,
            R.string.filterTriple
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.CARTOON),
            R.drawable.img_filter_cartoon,
            R.string.filterCartoon
        ),

        FilterDisplayData(
            FilterDisplayId.create(GlFilterCode.WATER_REFLECTION),
            R.drawable.img_filter_water_reflection,
            R.string.filterWaterReflection
        )
    )

    override operator fun get(index: Int) = items[index]

    override operator fun get(code: GlFilterCode) = items.single { it.id.filterCode == code }

    override fun exists(code: GlFilterCode) = items.any { it.id.filterCode == code }

    override fun <T>map(mapActon: (FilterDisplayData) -> T): List<T> = items.map { mapActon(it) }

    override fun getIndex(code: GlFilterCode): Int = items.indexOfFirst { it.id.filterCode == code }
}