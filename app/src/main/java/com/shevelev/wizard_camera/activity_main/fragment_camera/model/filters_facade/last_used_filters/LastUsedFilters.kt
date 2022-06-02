package com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.last_used_filters

import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode

internal interface LastUsedFilters {
    operator fun get(group: FiltersGroup): GlFilterCode?

    suspend fun init()

    suspend fun update(code: GlFilterCode, group: FiltersGroup)

    suspend fun remove(group: FiltersGroup)
}