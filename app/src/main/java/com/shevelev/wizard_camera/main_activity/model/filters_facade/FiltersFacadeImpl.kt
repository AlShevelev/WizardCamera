package com.shevelev.wizard_camera.main_activity.model.filters_facade

import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.common_entities.entities.LastUsedFilter
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.EmptyFilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.main_activity.dto.FilterFavoriteType
import com.shevelev.wizard_camera.main_activity.dto.FilterListItem
import com.shevelev.wizard_camera.main_activity.dto.FiltersListData
import com.shevelev.wizard_camera.main_activity.dto.FiltersMode
import com.shevelev.wizard_camera.main_activity.model.filters_facade.display_data.FilterDisplayDataList
import com.shevelev.wizard_camera.main_activity.model.filters_facade.settings.FilterSettingsFacade
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.storage.repositories.FavoriteFilterRepository
import com.shevelev.wizard_camera.storage.repositories.LastUsedFilterRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FiltersFacadeImpl
@Inject
constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val lastUsedFilterRepository: LastUsedFilterRepository,
    private val favoriteFilterRepository: FavoriteFilterRepository,
    private val displayData: FilterDisplayDataList,
    private val filterSettings: FilterSettingsFacade
) : FiltersFacade {

    private lateinit var favoritesList: MutableList<FilterCode>

    private var selectedFilter = displayData[0].code
    private var selectedFavoriteFilter = FilterCode.ORIGINAL

    private var priorFavoritesListData: FiltersListData? = null

    override val displayFilter: FilterSettings
        get() = when(filtersMode) {
            FiltersMode.NO_FILTERS -> filterSettings[FilterCode.ORIGINAL]
            FiltersMode.ALL-> filterSettings[selectedFilter]
            FiltersMode.FAVORITE -> filterSettings[selectedFavoriteFilter]
        }

    override val displayFilterTitle: Int
        get() = when(filtersMode) {
            FiltersMode.NO_FILTERS -> R.string.filterOriginal
            FiltersMode.ALL-> displayData[selectedFilter].title
            FiltersMode.FAVORITE -> if(selectedFavoriteFilter == FilterCode.ORIGINAL) {
                R.string.filterOriginal
            } else {
                displayData[selectedFavoriteFilter].title
            }
        }

    override var filtersMode: FiltersMode = FiltersMode.NO_FILTERS

    override suspend fun init() {
        filterSettings.init()

        val lastUsedFilters = withContext(dispatchersProvider.ioDispatcher) {
            lastUsedFilterRepository.read()
        }

        favoritesList = withContext(dispatchersProvider.ioDispatcher) {
            favoriteFilterRepository.read()
        }.toMutableList()

        selectedFilter = lastUsedFilters.firstOrNull { !it.isFavorite }?.code ?: displayData[0].code

        selectedFavoriteFilter =
            lastUsedFilters.firstOrNull { it.isFavorite }?.code
            ?: favoritesList.firstOrNull()
            ?: FilterCode.ORIGINAL
    }

    override suspend fun selectFilter(code: FilterCode) {
        withContext(dispatchersProvider.ioDispatcher) {
            lastUsedFilterRepository.update(LastUsedFilter(code, false))
        }

        selectedFilter = code
    }

    override suspend fun selectFavoriteFilter(code: FilterCode) {
        withContext(dispatchersProvider.ioDispatcher) {
            lastUsedFilterRepository.update(LastUsedFilter(code, true))
        }

        selectedFavoriteFilter = code
    }

    override suspend fun getAllFiltersListData(): FiltersListData {
        val startItems = displayData.map {
            val isFavorite = if(favoritesList.contains(it.code)) FilterFavoriteType.FAVORITE else FilterFavoriteType.NOT_FAVORITE
            FilterListItem(it, isFavorite, filterSettings[it.code] !is EmptyFilterSettings)
        }

        return FiltersListData(displayData.getIndex(selectedFilter), startItems)
    }

    override suspend fun getFavoriteFiltersListData(): FiltersListData? {
        var startIndex = favoritesList.indexOf(selectedFavoriteFilter)

        val items = favoritesList.map {
            FilterListItem(displayData[it], FilterFavoriteType.HIDDEN, filterSettings[it] !is EmptyFilterSettings)
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

    override suspend fun addToFavorite(code: FilterCode) {
        withContext(dispatchersProvider.ioDispatcher) {
            favoriteFilterRepository.create(code)
        }

        if(!favoritesList.contains(code)) {
            favoritesList.add(code)
        }

        if(selectedFavoriteFilter == FilterCode.ORIGINAL) {
            selectedFavoriteFilter = code
        }
    }

    override suspend fun removeFromFavorite(code: FilterCode) {
        withContext(dispatchersProvider.ioDispatcher) {
            favoriteFilterRepository.delete(code)
        }

        favoritesList.remove(code)

        if(favoritesList.isEmpty()) {
            selectedFavoriteFilter = FilterCode.ORIGINAL

            withContext(dispatchersProvider.ioDispatcher) {
                lastUsedFilterRepository.remove(LastUsedFilter(code, true))
            }
        } else {
            if(selectedFavoriteFilter == code) {
                selectedFavoriteFilter = favoritesList[0]

                withContext(dispatchersProvider.ioDispatcher) {
                    lastUsedFilterRepository.update(LastUsedFilter(selectedFavoriteFilter, true))
                }
            }
        }
    }

    override fun getSettings(code: FilterCode) = filterSettings[code]

    override suspend fun updateSettings(settings: FilterSettings) = filterSettings.update(settings)
}