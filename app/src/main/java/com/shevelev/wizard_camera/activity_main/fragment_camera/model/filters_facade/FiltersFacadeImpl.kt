package com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade

import android.content.Context
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto.FiltersMode
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.groups.FiltersGroupStorage
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.last_used_filters.LastUsedFilters
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.settings.FilterSettingsFacade
import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl.FilterDisplayDataList
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel.FilterListItem
import com.shevelev.wizard_camera.core.ui_kit.lib.flower_menu.FlowerMenuItemData

internal class FiltersFacadeImpl (
    private val context: Context,
    private val lastUsedFilters: LastUsedFilters,
    private val displayData: FilterDisplayDataList,
    private val filterSettings: FilterSettingsFacade,
    private val groups: Map<FiltersGroup, FiltersGroupStorage>
) : FiltersFacade {

    override val displayFilter: GlFilterSettings
        get() = when(filtersMode) {
            FiltersMode.NO_FILTERS -> filterSettings[GlFilterCode.ORIGINAL]
            FiltersMode.ALL-> filterSettings[groups[FiltersGroup.ALL]!!.selected]
            FiltersMode.FAVORITE -> filterSettings[groups[FiltersGroup.FAVORITES]!!.selected]
        }

    override val displayFilterTitle: Int
        get() = when(filtersMode) {
            FiltersMode.NO_FILTERS -> R.string.filterOriginal
            FiltersMode.ALL-> displayData[groups[FiltersGroup.ALL]!!.selected].title
            FiltersMode.FAVORITE -> if(groups[FiltersGroup.FAVORITES]!!.selected == GlFilterCode.ORIGINAL) {
                R.string.filterOriginal
            } else {
                displayData[groups[FiltersGroup.FAVORITES]!!.selected].title
            }
        }

    override var filtersMode: FiltersMode = FiltersMode.NO_FILTERS

    override suspend fun init() {
        filterSettings.init()
        lastUsedFilters.init()

        groups[FiltersGroup.FAVORITES]!!.init() // We need to init Favorites first because other groups depend on it
        groups.forEach {
            if(it.key != FiltersGroup.FAVORITES) {
                it.value.init()
            }
        }
    }

    override fun getFiltersForMenu(): List<FlowerMenuItemData> =
        with(context.resources) {
            listOf(
                FlowerMenuItemData(R.drawable.ic_gallery_no_filters, getString(R.string.filterOriginal)),
                FlowerMenuItemData(R.drawable.ic_gallery_filter, getString(R.string.filterAll)),
                FlowerMenuItemData(R.drawable.ic_deformation, getString(R.string.filterDeformations)),
                FlowerMenuItemData(R.drawable.ic_color, getString(R.string.filterColors)),
                FlowerMenuItemData(R.drawable.ic_style, getString(R.string.filterStylization)),
                FlowerMenuItemData(R.drawable.ic_favorite, getString(R.string.filterFavorites))
            )
        }

    override suspend fun selectFilter(code: GlFilterCode, group: FiltersGroup) = groups[group]!!.select(code)

    override suspend fun getFiltersListData(group: FiltersGroup): List<FilterListItem> = groups[group]!!.getFilters()

    override suspend fun addToFavorite(code: GlFilterCode) {
        groups.forEach {
            it.value.addToFavorite(code)
        }
    }

    override suspend fun removeFromFavorite(code: GlFilterCode) {
        groups.forEach {
            it.value.removeFromFavorite(code)
        }
    }

    override fun getSettings(code: GlFilterCode) = filterSettings[code]

    override suspend fun updateSettings(settings: GlFilterSettings) = filterSettings.update(settings)
}