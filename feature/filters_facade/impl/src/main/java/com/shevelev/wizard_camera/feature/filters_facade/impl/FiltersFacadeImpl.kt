package com.shevelev.wizard_camera.feature.filters_facade.impl

import android.content.Context
import com.shevelev.wizard_camera.feature.filters_facade.impl.groups.FiltersGroupStorage
import com.shevelev.wizard_camera.feature.filters_facade.impl.last_used_filters.LastUsedFilters
import com.shevelev.wizard_camera.feature.filters_facade.impl.settings.FilterSettingsFacade
import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl.FilterDisplayDataList
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel.FilterListItem
import com.shevelev.wizard_camera.core.ui_kit.lib.flower_menu.FlowerMenuItemData
import com.shevelev.wizard_camera.feature.filters_facade.api.FiltersFacade

internal class FiltersFacadeImpl (
    private val context: Context,
    private val lastUsedFilters: LastUsedFilters,
    private val displayData: FilterDisplayDataList,
    private val filterSettings: FilterSettingsFacade,
    private val groups: Map<FiltersGroup, FiltersGroupStorage>
) : FiltersFacade {

    override val displayFilter: GlFilterSettings
        get() = filterSettings[groups[currentGroup]!!.selected]

    override val displayFilterTitle: Int
        get() {
            val selected = groups[currentGroup]!!.selected
            return if (selected == GlFilterCode.ORIGINAL) {
                R.string.filterOriginal
            } else {
                displayData[selected].title
            }
        }

    override var currentGroup: FiltersGroup = FiltersGroup.NO_FILTERS

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
            FiltersGroup.values().map {
                when(it) {
                    FiltersGroup.ALL ->
                        FlowerMenuItemData(R.drawable.ic_gallery_filter, getString(R.string.filterAll))

                    FiltersGroup.FAVORITES ->
                        FlowerMenuItemData(R.drawable.ic_favorite, getString(R.string.filterFavorites))

                    FiltersGroup.NO_FILTERS ->
                        FlowerMenuItemData(R.drawable.ic_gallery_no_filters, getString(R.string.filterOriginal))

                    FiltersGroup.STYLIZATION ->
                        FlowerMenuItemData(R.drawable.ic_style, getString(R.string.filterStylization))

                    FiltersGroup.COLORS ->
                        FlowerMenuItemData(R.drawable.ic_color, getString(R.string.filterColors))

                    FiltersGroup.DEFORMATIONS ->
                        FlowerMenuItemData(R.drawable.ic_deformation, getString(R.string.filterDeformations))
                }
            }
        }

    override suspend fun selectFilter(code: GlFilterCode) = groups[currentGroup]!!.select(code)

    override suspend fun getFiltersListData(): List<FilterListItem> = groups[currentGroup]!!.getFilters()

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