package com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade

import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.core.common_entities.entities.LastUsedFilter
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.EmptyFilterSettings
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.filters.filters_carousel.FilterFavoriteType
import com.shevelev.wizard_camera.filters.filters_carousel.FilterListItem
import com.shevelev.wizard_camera.filters.filters_carousel.FiltersListData
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto.FiltersMode
import com.shevelev.wizard_camera.filters.display_data.gl.FilterDisplayDataList
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.settings.FilterSettingsFacade
import com.shevelev.wizard_camera.core.database.api.repositories.FavoriteFilterDbRepository
import com.shevelev.wizard_camera.core.database.api.repositories.LastUsedFilterDbRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FiltersFacadeImpl (
    private val lastUsedFilterRepository: LastUsedFilterDbRepository,
    private val favoriteFilterRepository: FavoriteFilterDbRepository,
    private val displayData: FilterDisplayDataList,
    private val filterSettings: FilterSettingsFacade
) : FiltersFacade {

    private lateinit var favoritesList: MutableList<GlFilterCode>

    private var selectedFilter = displayData[0].code
    private var selectedFavoriteFilter = GlFilterCode.ORIGINAL

    private var priorFavoritesListData: FiltersListData? = null

    override val displayFilter: GlFilterSettings
        get() = when(filtersMode) {
            FiltersMode.NO_FILTERS -> filterSettings[GlFilterCode.ORIGINAL]
            FiltersMode.ALL-> filterSettings[selectedFilter]
            FiltersMode.FAVORITE -> filterSettings[selectedFavoriteFilter]
        }

    override val displayFilterTitle: Int
        get() = when(filtersMode) {
            FiltersMode.NO_FILTERS -> R.string.filterOriginal
            FiltersMode.ALL-> displayData[selectedFilter].title
            FiltersMode.FAVORITE -> if(selectedFavoriteFilter == GlFilterCode.ORIGINAL) {
                R.string.filterOriginal
            } else {
                displayData[selectedFavoriteFilter].title
            }
        }

    override var filtersMode: FiltersMode = FiltersMode.NO_FILTERS

    override suspend fun init() {
        filterSettings.init()

        val lastUsedFilters = withContext(Dispatchers.IO) {
            lastUsedFilterRepository.read()
        }

        favoritesList = withContext(Dispatchers.IO) {
            favoriteFilterRepository.read()
        }.toMutableList()

        selectedFilter = lastUsedFilters.firstOrNull { !it.isFavorite }?.code ?: displayData[0].code

        selectedFavoriteFilter =
            lastUsedFilters.firstOrNull { it.isFavorite }?.code
            ?: favoritesList.firstOrNull()
            ?: GlFilterCode.ORIGINAL
    }

    override suspend fun selectFilter(code: GlFilterCode) {
        withContext(Dispatchers.IO) {
            lastUsedFilterRepository.update(LastUsedFilter(code, false))
        }

        selectedFilter = code
    }

    override suspend fun selectFavoriteFilter(code: GlFilterCode) {
        withContext(Dispatchers.IO) {
            lastUsedFilterRepository.update(LastUsedFilter(code, true))
        }

        selectedFavoriteFilter = code
    }

    override suspend fun getAllFiltersListData(): FiltersListData {
        val startItems = displayData.map {
            val isFavorite = if(favoritesList.contains(it.code)) {
                FilterFavoriteType.FAVORITE
            }
            else {
                FilterFavoriteType.NOT_FAVORITE
            }

            FilterListItem(
                listId = FiltersListId.ALL_FILTERS_LIST,
                displayData = it,
                favorite = isFavorite,
                hasSettings = filterSettings[it.code] !is EmptyFilterSettings,
                isSelected = false
            )
        }

        return FiltersListData(displayData.getIndex(selectedFilter), startItems)
    }

    override suspend fun getFavoriteFiltersListData(): FiltersListData? {
        var startIndex = favoritesList.indexOf(selectedFavoriteFilter)

        val items = favoritesList.map {
            FilterListItem(
                listId = FiltersListId.FAVORITE_FILTERS_LIST,
                displayData = displayData[it],
                favorite = FilterFavoriteType.HIDDEN,
                hasSettings = filterSettings[it] !is EmptyFilterSettings,
                isSelected = false
            )
        }

        if(items.isNotEmpty() && startIndex == -1) {
            startIndex = 0
            selectedFavoriteFilter = items[0].displayData.code
        }

        val newFavoritesListData = FiltersListData(startIndex, items)

        return if(newFavoritesListData != priorFavoritesListData) {
            priorFavoritesListData = newFavoritesListData
            newFavoritesListData
        } else {
            null
        }
    }

    override suspend fun addToFavorite(code: GlFilterCode) {
        withContext(Dispatchers.IO) {
            favoriteFilterRepository.create(code)
        }

        if(!favoritesList.contains(code)) {
            favoritesList.add(code)
        }

        if(selectedFavoriteFilter == GlFilterCode.ORIGINAL) {
            selectedFavoriteFilter = code
        }
    }

    override suspend fun removeFromFavorite(code: GlFilterCode) {
        withContext(Dispatchers.IO) {
            favoriteFilterRepository.delete(code)
        }

        favoritesList.remove(code)

        if(favoritesList.isEmpty()) {
            selectedFavoriteFilter = GlFilterCode.ORIGINAL

            withContext(Dispatchers.IO) {
                lastUsedFilterRepository.remove(LastUsedFilter(code, true))
            }
        } else {
            if(selectedFavoriteFilter == code) {
                selectedFavoriteFilter = favoritesList[0]

                withContext(Dispatchers.IO) {
                    lastUsedFilterRepository.update(LastUsedFilter(selectedFavoriteFilter, true))
                }
            }
        }
    }

    override fun getSettings(code: GlFilterCode) = filterSettings[code]

    override suspend fun updateSettings(settings: GlFilterSettings) = filterSettings.update(settings)
}