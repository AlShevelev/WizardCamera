package com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.display_data

import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.shared.filters_ui.dto.FilterDisplayData

interface FilterDisplayDataList {
    operator fun get(index: Int): FilterDisplayData

    operator fun get(code: GlFilterCode): FilterDisplayData

    fun exists(code: GlFilterCode): Boolean

    fun <T>map(mapActon: (FilterDisplayData) -> T): List<T>

    fun getIndex(code: GlFilterCode): Int
}