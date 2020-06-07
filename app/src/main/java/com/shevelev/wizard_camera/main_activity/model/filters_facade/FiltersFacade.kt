package com.shevelev.wizard_camera.main_activity.model.filters_facade

import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.main_activity.dto.FiltersListData
import com.shevelev.wizard_camera.main_activity.dto.FiltersMode

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
}