package com.shevelev.wizard_camera.main_activity.model.filters_repository

import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.main_activity.dto.FiltersListItem

interface FiltersRepository {
    val selectedFilter: FilterCode

    val selectedFilterTitle: Int

    val isFilterTurnedOn: Boolean

    val currentFilterIndex: Int

    val items: List<FiltersListItem>

    fun selectNextFilter()

    fun selectPriorFilter()

    fun switchMode()
}