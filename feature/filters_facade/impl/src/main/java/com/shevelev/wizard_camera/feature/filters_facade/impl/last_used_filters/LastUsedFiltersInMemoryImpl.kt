package com.shevelev.wizard_camera.feature.filters_facade.impl.last_used_filters

import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode

internal class LastUsedFiltersInMemoryImpl : LastUsedFilters {
    private val filters = mutableMapOf<FiltersGroup, GlFilterCode>()

    override fun get(group: FiltersGroup): GlFilterCode? = filters[group]

    override suspend fun init() {
        // do nothing
    }

    override suspend fun update(code: GlFilterCode, group: FiltersGroup) {
        filters[group] = code
    }

    override suspend fun remove(group: FiltersGroup) {
        filters.remove(group)
    }
}