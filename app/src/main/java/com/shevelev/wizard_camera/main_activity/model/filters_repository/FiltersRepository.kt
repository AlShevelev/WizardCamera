package com.shevelev.wizard_camera.main_activity.model.filters_repository

import com.shevelev.wizard_camera.camera.filter.FilterCode

interface FiltersRepository {
    val selectedFilter: FilterCode

    val selectedFilterTitle: Int

    fun selectNext()

    fun selectPrior()
}