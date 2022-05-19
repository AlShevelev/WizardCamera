package com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade

import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto.FiltersMode
import com.shevelev.wizard_camera.filters.filters_carousel.FilterListItem

interface FiltersFacade {
    val displayFilter: GlFilterSettings

    val displayFilterTitle: Int

    var filtersMode: FiltersMode

    suspend fun init()

    suspend fun selectFilter(code: GlFilterCode)

    suspend fun selectFavoriteFilter(code: GlFilterCode)

    suspend fun getAllFiltersListData(): List<FilterListItem>

    suspend fun getFavoriteFiltersListData(): List<FilterListItem>

    suspend fun addToFavorite(code: GlFilterCode)

    suspend fun removeFromFavorite(code: GlFilterCode)

    fun getSettings(code: GlFilterCode): GlFilterSettings

    suspend fun updateSettings(settings: GlFilterSettings)
}