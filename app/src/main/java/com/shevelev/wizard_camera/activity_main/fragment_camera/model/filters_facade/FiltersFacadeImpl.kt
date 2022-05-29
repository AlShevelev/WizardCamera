package com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade

import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto.FiltersMode
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.settings.FilterSettingsFacade
import com.shevelev.wizard_camera.core.common_entities.entities.LastUsedFilter
import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.EmptyFilterSettings
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.database.api.repositories.FavoriteFilterDbRepository
import com.shevelev.wizard_camera.core.database.api.repositories.LastUsedFilterDbRepository
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl.FilterDisplayDataList
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel.FilterFavoriteType
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel.FilterListItem
import com.shevelev.wizard_camera.core.ui_kit.lib.flower_menu.FlowerMenuItemData
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

        selectedFilter = lastUsedFilters.firstOrNull { it.group == FiltersGroup.ALL }?.code ?: displayData[0].code

        selectedFavoriteFilter =
            lastUsedFilters.firstOrNull { it.group == FiltersGroup.FAVORITES }?.code
            ?: favoritesList.firstOrNull()
            ?: GlFilterCode.ORIGINAL
    }

    override fun getFiltersForMenu(): List<FlowerMenuItemData> {
        TODO("Not yet implemented")
//        val items = listOf(
//            FlowerMenuItemData(R.drawable.ic_emoji_nature, "Text 1"),
//            FlowerMenuItemData(R.drawable.ic_emoji_nature, "text 2"),
//            FlowerMenuItemData(R.drawable.ic_emoji_nature, "Text 3"),
//            FlowerMenuItemData(R.drawable.ic_emoji_nature, "Text 4"),
//            FlowerMenuItemData(R.drawable.ic_emoji_nature, "Text 5"),
//            FlowerMenuItemData(R.drawable.ic_emoji_nature, "Text 6")
//        )

    }

    override suspend fun selectFilter(code: GlFilterCode, group: FiltersGroup) {
        TODO("Not yet implemented")
    }

    override suspend fun getFiltersListData(group: FiltersGroup): List<FilterListItem> {
        TODO("Not yet implemented")
    }

    override suspend fun selectFilter(code: GlFilterCode) {
        withContext(Dispatchers.IO) {
            lastUsedFilterRepository.update(LastUsedFilter(code, FiltersGroup.ALL))
        }

        selectedFilter = code
    }

    override suspend fun selectFavoriteFilter(code: GlFilterCode) {
        withContext(Dispatchers.IO) {
            lastUsedFilterRepository.update(LastUsedFilter(code, FiltersGroup.FAVORITES))
        }

        selectedFavoriteFilter = code
    }

    override suspend fun getAllFiltersListData(): List<FilterListItem> =
        displayData.map {
            val isFavorite = if(favoritesList.contains(it.code)) {
                FilterFavoriteType.FAVORITE
            }
            else {
                FilterFavoriteType.NOT_FAVORITE
            }

            FilterListItem(
                listId = FiltersGroup.ALL.toString(),
                displayData = it,
                favorite = isFavorite,
                hasSettings = filterSettings[it.code] !is EmptyFilterSettings,
                isSelected = selectedFilter == it.code
            )
        }

    override suspend fun getFavoriteFiltersListData(): List<FilterListItem> {
        val startIndex = favoritesList.indexOf(selectedFavoriteFilter)

        val items = favoritesList.mapIndexed { index, item ->
            FilterListItem(
                listId = FiltersGroup.FAVORITES.toString(),
                displayData = displayData[item],
                favorite = FilterFavoriteType.HIDDEN,
                hasSettings = filterSettings[item] !is EmptyFilterSettings,
                isSelected = if(startIndex == -1) {
                    index == 0
                } else {
                    selectedFavoriteFilter == displayData[item].code
                }
            )
        }

        if(items.isNotEmpty() && startIndex == -1) {
            selectedFavoriteFilter = items[0].displayData.code
        }

        return items
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
                lastUsedFilterRepository.remove(LastUsedFilter(code, FiltersGroup.FAVORITES))
            }
        } else {
            if(selectedFavoriteFilter == code) {
                selectedFavoriteFilter = favoritesList[0]

                withContext(Dispatchers.IO) {
                    lastUsedFilterRepository.update(LastUsedFilter(selectedFavoriteFilter, FiltersGroup.FAVORITES))
                }
            }
        }
    }

    override fun getSettings(code: GlFilterCode) = filterSettings[code]

    override suspend fun updateSettings(settings: GlFilterSettings) = filterSettings.update(settings)
}