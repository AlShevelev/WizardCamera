package com.shevelev.wizard_camera.core.database.api.repositories

import com.shevelev.wizard_camera.core.common_entities.entities.LastUsedFilter

interface LastUsedFilterRepository {
    fun update(filter: LastUsedFilter)

    fun remove(filter: LastUsedFilter)

    fun read(): List<LastUsedFilter>
}