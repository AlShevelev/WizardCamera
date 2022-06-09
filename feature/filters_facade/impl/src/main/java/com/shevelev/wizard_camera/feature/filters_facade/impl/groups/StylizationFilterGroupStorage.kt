package com.shevelev.wizard_camera.feature.filters_facade.impl.groups

import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl.FilterDisplayDataList
import com.shevelev.wizard_camera.feature.filters_facade.impl.last_used_filters.LastUsedFilters
import com.shevelev.wizard_camera.feature.filters_facade.impl.settings.FilterSettingsFacade

internal class StylizationFilterGroupStorage(
    favoriteFilters: FiltersGroupStorage,
    displayData: FilterDisplayDataList,
    filterSettings: FilterSettingsFacade,
    lastUsedFilters: LastUsedFilters
) : FiltersGroupStorageBase(
    favoriteFilters,
    displayData,
    filterSettings,
    lastUsedFilters,
    FiltersGroup.STYLIZATION
), FiltersGroupStorage {

    override fun getSupportedFilters(): List<GlFilterCode> =
        listOf(
            GlFilterCode.EDGE_DETECTION,
            GlFilterCode.PIXELIZE,
            GlFilterCode.TRIANGLES_MOSAIC,
            GlFilterCode.LEGOFIED,
            GlFilterCode.TILE_MOSAIC,
            GlFilterCode.MAPPING,
            GlFilterCode.CROSSHATCH,
            GlFilterCode.NEWSPAPER,
            GlFilterCode.ASCII_ART,
            GlFilterCode.MONEY,
            GlFilterCode.CRACKED,
            GlFilterCode.RELIEF,
            GlFilterCode.HEXAGON_MOSAIC
        )
}