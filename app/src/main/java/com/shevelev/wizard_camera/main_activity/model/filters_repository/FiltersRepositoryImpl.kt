package com.shevelev.wizard_camera.main_activity.model.filters_repository

import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.main_activity.dto.FilterListStartData
import com.shevelev.wizard_camera.main_activity.dto.FiltersListItem
import javax.inject.Inject

class FiltersRepositoryImpl
@Inject
constructor() : FiltersRepository {
    private val items = listOf(
        FiltersListItem(FilterCode.EDGE_DETECTION, R.drawable.img_filter_edge_detection, R.string.filterEdgeDectection),
        FiltersListItem(FilterCode.PIXELIZE, R.drawable.img_filter_pixelize, R.string.filterPixelize),
        FiltersListItem(FilterCode.EM_INTERFERENCE, R.drawable.img_filter_em_interference, R.string.filterEMInterference),
        FiltersListItem(FilterCode.TRIANGLES_MOSAIC, R.drawable.img_filter_triangles_mosaic, R.string.filterTrianglesMosaic),
        FiltersListItem(FilterCode.LEGOFIED, R.drawable.img_filter_legofied, R.string.filterLegofied),
        FiltersListItem(FilterCode.TILE_MOSAIC, R.drawable.img_filter_tile_mosaic, R.string.filterTileMosaic),
        FiltersListItem(FilterCode.BLUE_ORANGE, R.drawable.img_filter_blue_orange, R.string.filterBlueorange),
        FiltersListItem(FilterCode.CHROMATIC_ABERRATION, R.drawable.img_filter_chromatic_aberration, R.string.filterChromaticAberration),
        FiltersListItem(FilterCode.BASIC_DEFORM, R.drawable.img_filter_basic_deform, R.string.filterBasicDeform),
        FiltersListItem(FilterCode.CONTRAST, R.drawable.img_filter_contrast, R.string.filterContrast),
        FiltersListItem(FilterCode.NOISE_WARP, R.drawable.img_filter_noise_warp, R.string.filterNoiseWarp),
        FiltersListItem(FilterCode.REFRACTION, R.drawable.img_filter_refraction, R.string.filterRefraction),
        FiltersListItem(FilterCode.MAPPING, R.drawable.img_filter_mapping, R.string.filterMapping),
        FiltersListItem(FilterCode.CROSSHATCH, R.drawable.img_filter_crosshatch, R.string.filterCrosshatch),
        FiltersListItem(FilterCode.LICHTENSTEIN_ESQUE, R.drawable.img_filter_lichtenstein_esque, R.string.filterLichtensteinEsque),
        FiltersListItem(FilterCode.ASCII_ART, R.drawable.img_filter_ascii_art, R.string.filterAsciiArt),
        FiltersListItem(FilterCode.MONEY, R.drawable.img_filter_money, R.string.filterMoney),
        FiltersListItem(FilterCode.CRACKED, R.drawable.img_filter_cracked, R.string.filterCracked),
        FiltersListItem(FilterCode.POLYGONIZATION, R.drawable.img_filter_polygonization, R.string.filterPolygonization),
        FiltersListItem(FilterCode.BLACK_AND_WHITE, R.drawable.img_filter_black_and_white, R.string.filterBlackAndWhite),
        FiltersListItem(FilterCode.GRAY, R.drawable.img_filter_gray, R.string.filterGray),
        FiltersListItem(FilterCode.NEGATIVE, R.drawable.img_filter_negative, R.string.filterNegative),
        FiltersListItem(FilterCode.NOSTALGIA, R.drawable.img_filter_nostalgia, R.string.filterNostalgia),
        FiltersListItem(FilterCode.CASTING, R.drawable.img_filter_casting, R.string.filterCasting),
        FiltersListItem(FilterCode.RELIEF, R.drawable.img_filter_relief, R.string.filterRelief),
        FiltersListItem(FilterCode.SWIRL, R.drawable.img_filter_swirl, R.string.filterSwirl),
        FiltersListItem(FilterCode.HEXAGON_MOSAIC, R.drawable.img_filter_hexagon_mosaic, R.string.filterHexagonMosaic),
        FiltersListItem(FilterCode.MIRROR, R.drawable.img_filter_mirror, R.string.filterMirror),
        FiltersListItem(FilterCode.TRIPLE, R.drawable.img_filter_triple, R.string.filterTriple),
        FiltersListItem(FilterCode.CARTOON, R.drawable.img_filter_cartoon, R.string.filterCartoon),
        FiltersListItem(FilterCode.WATER_REFLECTION, R.drawable.img_filter_water_reflection, R.string.filterWaterReflection)
    )

    private var selectedFilter: FilterCode = FilterCode.EDGE_DETECTION
    private var selectedFilterTitle: Int = items[0].title

    override val displayFilter: FilterCode
        get() = if(isFilterTurnedOn) selectedFilter else FilterCode.ORIGINAL

    override val displayFilterTitle: Int
        get() = if(isFilterTurnedOn) selectedFilterTitle else R.string.filterOriginal

    override var isFilterTurnedOn: Boolean = false
        private set

    override fun selectFilter(code: FilterCode) {
        selectedFilter = code
        selectedFilterTitle = items.single { it.code == code }.title
    }

    override fun switchMode() {
        isFilterTurnedOn = !isFilterTurnedOn
    }

    override fun getStartData(): FilterListStartData {
        return FilterListStartData(items.indexOfFirst { it.code == selectedFilter }, items)
    }
}