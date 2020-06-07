package com.shevelev.wizard_camera.main_activity.model.filters_facade.display_data

import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.main_activity.dto.FilterDisplayData

interface FilterDisplayDataList {
    operator fun get(index: Int): FilterDisplayData

    operator fun get(code: FilterCode): FilterDisplayData

    fun exists(code: FilterCode): Boolean

    fun <T>map(mapActon: (FilterDisplayData) -> T): List<T>

    fun getIndex(code: FilterCode): Int
}