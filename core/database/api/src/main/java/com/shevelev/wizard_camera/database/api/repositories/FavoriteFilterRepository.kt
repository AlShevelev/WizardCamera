package com.shevelev.wizard_camera.database.api.repositories

import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode

interface FavoriteFilterRepository {
    fun create(code: GlFilterCode)

    fun read(): List<GlFilterCode>

    fun delete(code: GlFilterCode)
}