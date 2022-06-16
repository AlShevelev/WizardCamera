package com.shevelev.wizard_camera.feature.filters_facade.impl.groups

import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl.FilterDisplayDataList
import com.shevelev.wizard_camera.feature.filters_facade.impl.last_used_filters.LastUsedFilters
import com.shevelev.wizard_camera.feature.filters_facade.impl.settings.FilterSettingsFacade

internal class DeformationsFiltersGroupStorage(
    favoriteFilters: FiltersGroupStorage,
    displayData: FilterDisplayDataList,
    filterSettings: FilterSettingsFacade,
    lastUsedFilters: LastUsedFilters,
    canUpdateFavorites: Boolean
) : FiltersGroupStorageBase(
    favoriteFilters,
    displayData,
    filterSettings,
    lastUsedFilters,
    FiltersGroup.DEFORMATIONS,
    canUpdateFavorites
), FiltersGroupStorage {

    override fun getSupportedFilters(): List<GlFilterCode> =
        listOf(
            GlFilterCode.BASIC_DEFORM,
            GlFilterCode.NOISE_WARP,
            GlFilterCode.REFRACTION,
            GlFilterCode.POLYGONIZATION,
            GlFilterCode.SWIRL,
            GlFilterCode.MIRROR,
            GlFilterCode.TRIPLE,
            GlFilterCode.WATER_REFLECTION
        )
}