package com.shevelev.wizard_camera.storage.repositories

import com.shevelev.wizard_camera.common_entities.enums.FilterCode

interface FavoriteFilterRepository {
    fun create(code: FilterCode)

    fun read(): List<FilterCode>

    fun delete(code: FilterCode)
}