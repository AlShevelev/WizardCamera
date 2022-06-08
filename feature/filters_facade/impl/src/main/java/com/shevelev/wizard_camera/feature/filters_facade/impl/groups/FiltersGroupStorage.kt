package com.shevelev.wizard_camera.feature.filters_facade.impl.groups

import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel.FilterListItem

interface FiltersGroupStorage {
    val selected: GlFilterCode

    suspend fun init()

    fun getFilters(): List<FilterListItem>

    fun contains(code: GlFilterCode): Boolean

    suspend fun select(code: GlFilterCode)

    suspend fun addToFavorite(code: GlFilterCode)

    suspend fun removeFromFavorite(code: GlFilterCode)
}

