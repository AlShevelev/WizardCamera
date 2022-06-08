package com.shevelev.wizard_camera.feature.filters_facade.impl.last_used_filters

import com.shevelev.wizard_camera.core.common_entities.entities.LastUsedFilter
import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.database.api.repositories.LastUsedFilterDbRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LastUsedFiltersImpl(
    private val lastUsedFilterRepository: LastUsedFilterDbRepository
) : LastUsedFilters {

    private val filters = mutableMapOf<FiltersGroup, GlFilterCode>()

    override fun get(group: FiltersGroup): GlFilterCode? = filters[group]

    override suspend fun init() {
        withContext(Dispatchers.IO) {
            lastUsedFilterRepository
                .read()
                .forEach {
                    filters[it.group] = it.code
                }
        }
    }

    override suspend fun update(code: GlFilterCode, group: FiltersGroup) {
        withContext(Dispatchers.IO) {
            lastUsedFilterRepository.update(LastUsedFilter(code, group))
        }

        filters[group] = code
    }

    override suspend fun remove(group: FiltersGroup) {
        withContext(Dispatchers.IO) {
            filters.remove(group)?.let {
                lastUsedFilterRepository.remove(LastUsedFilter(it, group))
            }
        }
    }
}