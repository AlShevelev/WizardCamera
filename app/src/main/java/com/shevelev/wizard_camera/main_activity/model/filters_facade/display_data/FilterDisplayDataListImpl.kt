package com.shevelev.wizard_camera.main_activity.model.filters_facade.display_data

import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.main_activity.dto.FilterDisplayData
import javax.inject.Inject

class FilterDisplayDataListImpl
@Inject
constructor() : FilterDisplayDataList {
    private val items = listOf(
        FilterDisplayData(FilterCode.EDGE_DETECTION, R.drawable.img_filter_edge_detection, R.string.filterEdgeDetection),
        FilterDisplayData(FilterCode.PIXELIZE, R.drawable.img_filter_pixelize, R.string.filterPixelize),
        FilterDisplayData(FilterCode.EM_INTERFERENCE, R.drawable.img_filter_em_interference, R.string.filterEMInterference),
        FilterDisplayData(FilterCode.TRIANGLES_MOSAIC, R.drawable.img_filter_triangles_mosaic, R.string.filterTrianglesMosaic),
        FilterDisplayData(FilterCode.LEGOFIED, R.drawable.img_filter_legofied, R.string.filterLegofied),
        FilterDisplayData(FilterCode.TILE_MOSAIC, R.drawable.img_filter_tile_mosaic, R.string.filterTileMosaic),
        FilterDisplayData(FilterCode.BLUE_ORANGE, R.drawable.img_filter_blue_orange, R.string.filterBlueorange),
        FilterDisplayData(FilterCode.CHROMATIC_ABERRATION, R.drawable.img_filter_chromatic_aberration, R.string.filterChromaticAberration),
        FilterDisplayData(FilterCode.BASIC_DEFORM, R.drawable.img_filter_basic_deform, R.string.filterBasicDeform),
        FilterDisplayData(FilterCode.CONTRAST, R.drawable.img_filter_contrast, R.string.filterContrast),
        FilterDisplayData(FilterCode.NOISE_WARP, R.drawable.img_filter_noise_warp, R.string.filterNoiseWarp),
        FilterDisplayData(FilterCode.REFRACTION, R.drawable.img_filter_refraction, R.string.filterRefraction),
        FilterDisplayData(FilterCode.MAPPING, R.drawable.img_filter_mapping, R.string.filterMapping),
        FilterDisplayData(FilterCode.CROSSHATCH, R.drawable.img_filter_crosshatch, R.string.filterCrosshatch),
        FilterDisplayData(FilterCode.NEWSPAPER, R.drawable.img_filter_lichtenstein_esque, R.string.filterNewspaper),
        FilterDisplayData(FilterCode.ASCII_ART, R.drawable.img_filter_ascii_art, R.string.filterAsciiArt),
        FilterDisplayData(FilterCode.MONEY, R.drawable.img_filter_money, R.string.filterMoney),
        FilterDisplayData(FilterCode.CRACKED, R.drawable.img_filter_cracked, R.string.filterCracked),
        FilterDisplayData(FilterCode.POLYGONIZATION, R.drawable.img_filter_polygonization, R.string.filterPolygonization),
        FilterDisplayData(FilterCode.BLACK_AND_WHITE, R.drawable.img_filter_black_and_white, R.string.filterBlackAndWhite),
        FilterDisplayData(FilterCode.GRAY, R.drawable.img_filter_gray, R.string.filterGray),
        FilterDisplayData(FilterCode.NEGATIVE, R.drawable.img_filter_negative, R.string.filterNegative),
        FilterDisplayData(FilterCode.NOSTALGIA, R.drawable.img_filter_nostalgia, R.string.filterNostalgia),
        FilterDisplayData(FilterCode.CASTING, R.drawable.img_filter_casting, R.string.filterCasting),
        FilterDisplayData(FilterCode.RELIEF, R.drawable.img_filter_relief, R.string.filterRelief),
        FilterDisplayData(FilterCode.SWIRL, R.drawable.img_filter_swirl, R.string.filterSwirl),
        FilterDisplayData(FilterCode.HEXAGON_MOSAIC, R.drawable.img_filter_hexagon_mosaic, R.string.filterHexagonMosaic),
        FilterDisplayData(FilterCode.MIRROR, R.drawable.img_filter_mirror, R.string.filterMirror),
        FilterDisplayData(FilterCode.TRIPLE, R.drawable.img_filter_triple, R.string.filterTriple),
        FilterDisplayData(FilterCode.CARTOON, R.drawable.img_filter_cartoon, R.string.filterCartoon),
        FilterDisplayData(FilterCode.WATER_REFLECTION, R.drawable.img_filter_water_reflection, R.string.filterWaterReflection)
    )

    override operator fun get(index: Int) = items[index]

    override operator fun get(code: FilterCode) = items.single { it.code == code }

    override fun exists(code: FilterCode) = items.any { it.code == code }

    override fun <T>map(mapActon: (FilterDisplayData) -> T): List<T> = items.map { mapActon(it) }

    override fun getIndex(code: FilterCode): Int = items.indexOfFirst { it.code == code }
}