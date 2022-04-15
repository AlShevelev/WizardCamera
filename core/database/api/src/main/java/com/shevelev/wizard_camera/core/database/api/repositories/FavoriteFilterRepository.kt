package com.shevelev.wizard_camera.core.database.api.repositories

import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode

interface FavoriteFilterRepository {
    fun create(code: GlFilterCode)

    fun read(): List<GlFilterCode>

    fun delete(code: GlFilterCode)
}