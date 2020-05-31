package com.shevelev.wizard_camera.main_activity.model.filters_repository

import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.main_activity.dto.FilterListStartData

interface FiltersRepository {
    val displayFilter: FilterCode

    val displayFilterTitle: Int

    val isFilterTurnedOn: Boolean

    suspend fun init()

    suspend fun selectFilter(code: FilterCode)

    fun switchMode()

    fun getStartData(): FilterListStartData
}