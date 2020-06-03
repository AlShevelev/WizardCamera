package com.shevelev.wizard_camera.storage.repositories

import com.shevelev.wizard_camera.common_entities.entities.LastUsedFilter

interface LastUsedFilterRepository {
    fun update(filter: LastUsedFilter)

    fun read(): List<LastUsedFilter>
}