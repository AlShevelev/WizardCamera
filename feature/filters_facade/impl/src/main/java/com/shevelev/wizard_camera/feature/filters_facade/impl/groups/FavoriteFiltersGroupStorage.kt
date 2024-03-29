package com.shevelev.wizard_camera.feature.filters_facade.impl.groups

import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.EmptyFilterSettings
import com.shevelev.wizard_camera.core.database.api.repositories.FavoriteFilterDbRepository
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl.FilterDisplayDataList
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel.FilterFavoriteType
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel.FilterListItem
import com.shevelev.wizard_camera.core.utils.ext.update
import com.shevelev.wizard_camera.feature.filters_facade.impl.last_used_filters.LastUsedFilters
import com.shevelev.wizard_camera.feature.filters_facade.impl.settings.FilterSettingsFacade
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class FavoriteFiltersGroupStorage(
    private val favoriteFilterRepository: FavoriteFilterDbRepository,
    private val displayData: FilterDisplayDataList,
    private val filterSettings: FilterSettingsFacade,
    private val lastUsedFilters: LastUsedFilters
) : FiltersGroupStorage {
    private val groupCode = FiltersGroup.FAVORITES

    private lateinit var filters: MutableList<FilterListItem>

    override val selected: GlFilterCode
        get() = filters.firstOrNull { it.isSelected }?.displayData?.code ?: GlFilterCode.ORIGINAL

    override suspend fun init() {
        val allFavoritesList = withContext(Dispatchers.IO) {
            favoriteFilterRepository.read()
        }.toMutableList()

        val firstSelectedFilter = lastUsedFilters[groupCode] ?: allFavoritesList.firstOrNull()

        filters = allFavoritesList
            .map { item ->
                FilterListItem(
                    listId = groupCode.toString(),
                    displayData = displayData[item],
                    favorite = FilterFavoriteType.HIDDEN,
                    hasSettings = filterSettings[item] !is EmptyFilterSettings,
                    isSelected = firstSelectedFilter == displayData[item].code
                )
            }
            .toMutableList()
    }

    override fun getFilters(): List<FilterListItem> = filters.toList()

    override fun contains(code: GlFilterCode): Boolean = filters.any { it.displayData.code == code }

    override suspend fun select(code: GlFilterCode) {
        if (filters.isEmpty()) {
            return
        }

        filters
            .indexOfFirst { it.isSelected }
            .takeIf { it != -1 }
            ?.let { index ->
                filters.update(index) {
                    it.copy(isSelected = false)
                }
            }

        val filterIndex = filters
            .indexOfFirst { it.displayData.code == code }
            .takeIf { it != -1 }
            ?: 0

        lastUsedFilters.update(code, groupCode)

        filters.update(filterIndex) {
            it.copy(isSelected = true)
        }
    }

    override suspend fun addToFavorite(code: GlFilterCode) {
        if (filters.any { it.displayData.code == code }) {
            return
        }

        withContext(Dispatchers.IO) {
            favoriteFilterRepository.create(code)
        }

        filters.add(
            FilterListItem(
                listId = groupCode.toString(),
                displayData = displayData[code],
                favorite = FilterFavoriteType.HIDDEN,
                hasSettings = filterSettings[code] !is EmptyFilterSettings,
                isSelected = filters.isEmpty()
            )
        )
    }

    override suspend fun removeFromFavorite(code: GlFilterCode) {
        val isSelectedRemoved = selected == code

        withContext(Dispatchers.IO) {
            favoriteFilterRepository.delete(code)
        }

        filters.removeAll { it.displayData.code == code }

        if (filters.isEmpty()) {
            lastUsedFilters.remove(groupCode)
        } else {
            if (isSelectedRemoved) {
                filters.update(0) {
                    it.copy(isSelected = true)
                }

                lastUsedFilters.update(filters[0].displayData.code, groupCode)
            }
        }
    }
}