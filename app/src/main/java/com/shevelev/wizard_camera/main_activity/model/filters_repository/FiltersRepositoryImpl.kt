package com.shevelev.wizard_camera.main_activity.model.filters_repository

import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.main_activity.dto.FilterFavoriteType
import com.shevelev.wizard_camera.main_activity.dto.FilterListItem
import com.shevelev.wizard_camera.main_activity.dto.FilterListStartData
import com.shevelev.wizard_camera.main_activity.dto.FilterStaticData
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.storage.repositories.FavoriteFilterRepository
import com.shevelev.wizard_camera.storage.repositories.LastUsedFilterRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FiltersRepositoryImpl
@Inject
constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val lastUsedFilterRepository: LastUsedFilterRepository,
    private val favoriteFilterRepository: FavoriteFilterRepository
) : FiltersRepository {
    private val items = listOf(
        FilterStaticData(FilterCode.EDGE_DETECTION, R.drawable.img_filter_edge_detection, R.string.filterEdgeDectection),
        FilterStaticData(FilterCode.PIXELIZE, R.drawable.img_filter_pixelize, R.string.filterPixelize),
        FilterStaticData(FilterCode.EM_INTERFERENCE, R.drawable.img_filter_em_interference, R.string.filterEMInterference),
        FilterStaticData(FilterCode.TRIANGLES_MOSAIC, R.drawable.img_filter_triangles_mosaic, R.string.filterTrianglesMosaic),
        FilterStaticData(FilterCode.LEGOFIED, R.drawable.img_filter_legofied, R.string.filterLegofied),
        FilterStaticData(FilterCode.TILE_MOSAIC, R.drawable.img_filter_tile_mosaic, R.string.filterTileMosaic),
        FilterStaticData(FilterCode.BLUE_ORANGE, R.drawable.img_filter_blue_orange, R.string.filterBlueorange),
        FilterStaticData(FilterCode.CHROMATIC_ABERRATION, R.drawable.img_filter_chromatic_aberration, R.string.filterChromaticAberration),
        FilterStaticData(FilterCode.BASIC_DEFORM, R.drawable.img_filter_basic_deform, R.string.filterBasicDeform),
        FilterStaticData(FilterCode.CONTRAST, R.drawable.img_filter_contrast, R.string.filterContrast),
        FilterStaticData(FilterCode.NOISE_WARP, R.drawable.img_filter_noise_warp, R.string.filterNoiseWarp),
        FilterStaticData(FilterCode.REFRACTION, R.drawable.img_filter_refraction, R.string.filterRefraction),
        FilterStaticData(FilterCode.MAPPING, R.drawable.img_filter_mapping, R.string.filterMapping),
        FilterStaticData(FilterCode.CROSSHATCH, R.drawable.img_filter_crosshatch, R.string.filterCrosshatch),
        FilterStaticData(FilterCode.LICHTENSTEIN_ESQUE, R.drawable.img_filter_lichtenstein_esque, R.string.filterLichtensteinEsque),
        FilterStaticData(FilterCode.ASCII_ART, R.drawable.img_filter_ascii_art, R.string.filterAsciiArt),
        FilterStaticData(FilterCode.MONEY, R.drawable.img_filter_money, R.string.filterMoney),
        FilterStaticData(FilterCode.CRACKED, R.drawable.img_filter_cracked, R.string.filterCracked),
        FilterStaticData(FilterCode.POLYGONIZATION, R.drawable.img_filter_polygonization, R.string.filterPolygonization),
        FilterStaticData(FilterCode.BLACK_AND_WHITE, R.drawable.img_filter_black_and_white, R.string.filterBlackAndWhite),
        FilterStaticData(FilterCode.GRAY, R.drawable.img_filter_gray, R.string.filterGray),
        FilterStaticData(FilterCode.NEGATIVE, R.drawable.img_filter_negative, R.string.filterNegative),
        FilterStaticData(FilterCode.NOSTALGIA, R.drawable.img_filter_nostalgia, R.string.filterNostalgia),
        FilterStaticData(FilterCode.CASTING, R.drawable.img_filter_casting, R.string.filterCasting),
        FilterStaticData(FilterCode.RELIEF, R.drawable.img_filter_relief, R.string.filterRelief),
        FilterStaticData(FilterCode.SWIRL, R.drawable.img_filter_swirl, R.string.filterSwirl),
        FilterStaticData(FilterCode.HEXAGON_MOSAIC, R.drawable.img_filter_hexagon_mosaic, R.string.filterHexagonMosaic),
        FilterStaticData(FilterCode.MIRROR, R.drawable.img_filter_mirror, R.string.filterMirror),
        FilterStaticData(FilterCode.TRIPLE, R.drawable.img_filter_triple, R.string.filterTriple),
        FilterStaticData(FilterCode.CARTOON, R.drawable.img_filter_cartoon, R.string.filterCartoon),
        FilterStaticData(FilterCode.WATER_REFLECTION, R.drawable.img_filter_water_reflection, R.string.filterWaterReflection)
    )

    private var selectedFilter = items[0].code
    private var selectedFilterTitle: Int = items[0].title

    override val displayFilter: FilterCode
        get() = if(isFilterTurnedOn) selectedFilter else FilterCode.ORIGINAL

    override val displayFilterTitle: Int
        get() = if(isFilterTurnedOn) selectedFilterTitle else R.string.filterOriginal

    override var isFilterTurnedOn: Boolean = false
        private set

    override suspend fun init() {
        val filter = withContext(dispatchersProvider.ioDispatcher) {
            lastUsedFilterRepository.read()
        }

        if(filter != null) {
            selectedFilter = filter
            selectedFilterTitle = items.single { it.code == filter }.title
        } else {
            selectedFilter = FilterCode.EDGE_DETECTION
            selectedFilterTitle = items[0].title
        }
    }

    override suspend fun selectFilter(code: FilterCode) {
        withContext(dispatchersProvider.ioDispatcher) {
            lastUsedFilterRepository.update(code)
        }
        selectedFilter = code
        selectedFilterTitle = items.single { it.code == code }.title
    }

    override fun switchMode() {
        isFilterTurnedOn = !isFilterTurnedOn
    }

    override suspend fun getStartFiltersData(): FilterListStartData {
        val favoritesDb = withContext(dispatchersProvider.ioDispatcher) {
            favoriteFilterRepository.read()
        }

        val startItems = items.map {
            val isFavorite = if(favoritesDb.contains(it.code)) FilterFavoriteType.FAVORITE else FilterFavoriteType.NOT_FAVORITE
            FilterListItem(it, isFavorite)
        }

        return FilterListStartData(items.indexOfFirst { it.code == selectedFilter }, startItems)
    }

    override suspend fun addToFavorite(code: FilterCode) {
        withContext(dispatchersProvider.ioDispatcher) {
            favoriteFilterRepository.create(code)
        }
    }

    override suspend fun removeFromFavorite(code: FilterCode) {
        withContext(dispatchersProvider.ioDispatcher) {
            favoriteFilterRepository.delete(code)
        }
    }
}