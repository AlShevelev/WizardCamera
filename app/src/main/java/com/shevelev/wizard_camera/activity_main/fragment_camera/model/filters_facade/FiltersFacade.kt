package com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade

import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto.FiltersMode
import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel.FilterListItem
import com.shevelev.wizard_camera.core.ui_kit.lib.flower_menu.FlowerMenuItemData

interface FiltersFacade {
    val displayFilter: GlFilterSettings

    val displayFilterTitle: Int

    var currentGroup: FiltersGroup

    suspend fun init()

    fun getFiltersForMenu(): List<FlowerMenuItemData>

    suspend fun selectFilter(code: GlFilterCode)

    suspend fun getFiltersListData(): List<FilterListItem>

    suspend fun addToFavorite(code: GlFilterCode)

    suspend fun removeFromFavorite(code: GlFilterCode)

    fun getSettings(code: GlFilterCode): GlFilterSettings

    suspend fun updateSettings(settings: GlFilterSettings)
}