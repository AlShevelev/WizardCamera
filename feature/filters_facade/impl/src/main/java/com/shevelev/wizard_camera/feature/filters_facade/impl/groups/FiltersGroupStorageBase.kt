package com.shevelev.wizard_camera.feature.filters_facade.impl.groups

import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.EmptyFilterSettings
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl.FilterDisplayDataList
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel.FilterFavoriteType
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel.FilterListItem
import com.shevelev.wizard_camera.core.utils.ext.update
import com.shevelev.wizard_camera.feature.filters_facade.impl.last_used_filters.LastUsedFilters
import com.shevelev.wizard_camera.feature.filters_facade.impl.settings.FilterSettingsFacade

internal abstract class FiltersGroupStorageBase(
    private val favoriteFilters: FiltersGroupStorage,
    private val displayData: FilterDisplayDataList,
    private val filterSettings: FilterSettingsFacade,
    private val lastUsedFilters: LastUsedFilters,
    private val groupCode: FiltersGroup,
    private val canUpdateFavorites: Boolean
) : FiltersGroupStorage {

    private lateinit var filters: MutableList<FilterListItem>

    override val selected: GlFilterCode
        get() = filters.first { it.isSelected }.displayData.code

    override suspend fun init() {
        if (::filters.isInitialized) {
            return
        }

        val firstSelectedFilter = lastUsedFilters[groupCode] ?: getSupportedFilters().first()

        filters = getSupportedFilters()
            .map { filterCode ->
                val isFavorite = if (canUpdateFavorites) {
                    if (favoriteFilters.contains(filterCode)) {
                        FilterFavoriteType.FAVORITE
                    } else {
                        FilterFavoriteType.NOT_FAVORITE
                    }
                } else {
                    FilterFavoriteType.HIDDEN
                }

                FilterListItem(
                    listId = groupCode.toString(),
                    displayData = displayData[filterCode],
                    favorite = isFavorite,
                    hasSettings = filterSettings[filterCode] !is EmptyFilterSettings,
                    isSelected = firstSelectedFilter == filterCode
                )
            }
            .toMutableList()
    }

    override fun getFilters(): List<FilterListItem> = filters.toList()

    override fun contains(code: GlFilterCode): Boolean = filters.any { it.displayData.code == code }

    override suspend fun select(code: GlFilterCode) {
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

        lastUsedFilters.update(filters[filterIndex].displayData.code, groupCode)

        filters.update(filterIndex) {
            it.copy(isSelected = true)
        }
    }

    override suspend fun addToFavorite(code: GlFilterCode) {
        filters
            .indexOfFirst { it.displayData.code == code }
            .takeIf { it != -1 }
            ?.let { index ->
                filters.update(index) {
                    it.copy(favorite = FilterFavoriteType.FAVORITE)
                }
            }
    }

    override suspend fun removeFromFavorite(code: GlFilterCode) {
        filters
            .indexOfFirst { it.displayData.code == code }
            .takeIf { it != -1 }
            ?.let { index ->
                filters.update(index) {
                    it.copy(favorite = FilterFavoriteType.NOT_FAVORITE)
                }
            }
    }

    protected abstract fun getSupportedFilters(): List<GlFilterCode>
}