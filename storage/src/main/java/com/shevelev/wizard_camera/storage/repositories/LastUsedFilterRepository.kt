package com.shevelev.wizard_camera.storage.repositories

import com.shevelev.wizard_camera.common_entities.enums.FilterCode

interface LastUsedFilterRepository {
    fun update(code: FilterCode)

    fun read(): FilterCode?
}