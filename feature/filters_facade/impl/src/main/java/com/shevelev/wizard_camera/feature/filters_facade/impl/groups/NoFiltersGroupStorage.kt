package com.shevelev.wizard_camera.feature.filters_facade.impl.groups

import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel.FilterListItem

internal class NoFiltersGroupStorage : FiltersGroupStorage {
    override val selected: GlFilterCode
        get() = GlFilterCode.ORIGINAL

    override suspend fun init() {
        // do nothing
    }

    override fun getFilters(): List<FilterListItem> = listOf()

    override fun contains(code: GlFilterCode): Boolean = false

    override suspend fun select(code: GlFilterCode) {
        // do nothing
    }

    override suspend fun addToFavorite(code: GlFilterCode) {
        // do nothing
    }

    override suspend fun removeFromFavorite(code: GlFilterCode) {
        // do nothing
    }
}