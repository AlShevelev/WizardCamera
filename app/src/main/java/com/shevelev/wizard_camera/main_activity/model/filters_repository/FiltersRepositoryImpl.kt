package com.shevelev.wizard_camera.main_activity.model.filters_repository

import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.camera.filter.FilterCode
import javax.inject.Inject

class FiltersRepositoryImpl
@Inject
constructor() : FiltersRepository {
    private val filters = listOf(
        FilterCode.ORIGINAL, FilterCode.EDGE_DETECTION, FilterCode.PIXELIZE, FilterCode.EM_INTERFERENCE,
        FilterCode.TRIANGLES_MOSAIC, FilterCode.LEGOFIED, FilterCode.TILE_MOSAIC, FilterCode.BLUE_ORANGE,
        FilterCode.CHROMATIC_ABERRATION, FilterCode.BASIC_DEFORM, FilterCode.CONTRAST, FilterCode.NOISE_WARP,
        FilterCode.REFRACTION, FilterCode.MAPPING, FilterCode.CROSSHATCH, FilterCode.LICHTENSTEIN_ESQUE,
        FilterCode.ASCII_ART, FilterCode.MONEY, FilterCode.CRACKED, FilterCode.POLYGONIZATION,
        FilterCode.BLACK_AND_WHITE, FilterCode.GRAY, FilterCode.NEGATIVE, FilterCode.NOSTALGIA,
        FilterCode.CASTING, FilterCode.RELIEF, FilterCode.SWIRL, FilterCode.HEXAGON_MOSAIC,
        FilterCode.MIRROR, FilterCode.TRIPLE, FilterCode.CARTOON, FilterCode.WATER_REFLECTION
    )

    private val titles = listOf(
        R.string.filterOriginal, R.string.filterEdgeDectection, R.string.filterPixelize, R.string.filterEMInterference,
        R.string.filterTrianglesMosaic, R.string.filterLegofied, R.string.filterTileMosaic, R.string.filterBlueorange,
        R.string.filterChromaticAberration, R.string.filterBasicDeform, R.string.filterContrast, R.string.filterNoiseWarp,
        R.string.filterRefraction, R.string.filterMapping, R.string.filterCrosshatch, R.string.filterLichtensteinEsque,
        R.string.filterAsciiArt, R.string.filterMoney, R.string.filterCracked, R.string.filterPolygonization,
        R.string.filterBlackAndWhite, R.string.filterGray, R.string.filterNegative, R.string.filterNostalgia,
        R.string.filterCasting, R.string.filterRelief, R.string.filterSwirl, R.string.filterHexagonMosaic,
        R.string.filterMirror, R.string.filterTriple, R.string.filterCartoon, R.string.filterWaterReflection
    )

    private var currentIndex = 0

    override val selectedFilter: FilterCode
        get() = filters[currentIndex]

    override val selectedFilterTitle: Int
        get() = titles[currentIndex]

    override fun selectNext() {
        if(++currentIndex > filters.lastIndex) {
            currentIndex = 0
        }
    }

    override fun selectPrior() {
        if(--currentIndex < 0) {
            currentIndex = filters.lastIndex
        }
    }
}