package com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade

import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto.FiltersListData
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto.FiltersMode

interface FiltersFacade {
    val displayFilter: FilterSettings

    val displayFilterTitle: Int

    var filtersMode: FiltersMode

    suspend fun init()

    suspend fun selectFilter(code: FilterCode)

    suspend fun selectFavoriteFilter(code: FilterCode)

    suspend fun getAllFiltersListData(): FiltersListData

    suspend fun getFavoriteFiltersListData(): FiltersListData?

    suspend fun addToFavorite(code: FilterCode)

    suspend fun removeFromFavorite(code: FilterCode)

    fun getSettings(code: FilterCode): FilterSettings

    suspend fun updateSettings(settings: FilterSettings)
}