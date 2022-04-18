package com.shevelev.wizard_camera.filters.display_data.gl

import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.filters.display_data.FilterDisplayData

interface FilterDisplayDataList {
    operator fun get(index: Int): FilterDisplayData

    operator fun get(code: GlFilterCode): FilterDisplayData

    fun exists(code: GlFilterCode): Boolean

    fun <T>map(mapActon: (FilterDisplayData) -> T): List<T>

    fun getIndex(code: GlFilterCode): Int
}