package com.shevelev.wizard_camera.feature.filters_facade.impl.groups

import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl.FilterDisplayDataList
import com.shevelev.wizard_camera.feature.filters_facade.impl.last_used_filters.LastUsedFilters
import com.shevelev.wizard_camera.feature.filters_facade.impl.settings.FilterSettingsFacade

internal class AllFiltersGroupStorage(
    favoriteFilters: FiltersGroupStorage,
    displayData: FilterDisplayDataList,
    filterSettings: FilterSettingsFacade,
    lastUsedFilters: LastUsedFilters
) : FiltersGroupStorageBase(
        favoriteFilters,
        displayData,
        filterSettings,
        lastUsedFilters,
        FiltersGroup.ALL
    ),
    FiltersGroupStorage {

    override fun getSupportedFilters(): List<GlFilterCode> =
        listOf(
            GlFilterCode.EDGE_DETECTION,
            GlFilterCode.PIXELIZE,
            GlFilterCode.TRIANGLES_MOSAIC,
            GlFilterCode.LEGOFIED,
            GlFilterCode.TILE_MOSAIC,
            GlFilterCode.BLUE_ORANGE,
            GlFilterCode.BASIC_DEFORM,
            GlFilterCode.CONTRAST,
            GlFilterCode.NOISE_WARP,
            GlFilterCode.REFRACTION,
            GlFilterCode.MAPPING,
            GlFilterCode.CROSSHATCH,
            GlFilterCode.NEWSPAPER,
            GlFilterCode.ASCII_ART,
            GlFilterCode.MONEY,
            GlFilterCode.CRACKED,
            GlFilterCode.POLYGONIZATION,
            GlFilterCode.BLACK_AND_WHITE,
            GlFilterCode.GRAY,
            GlFilterCode.NEGATIVE,
            GlFilterCode.NOSTALGIA,
            GlFilterCode.CASTING,
            GlFilterCode.RELIEF,
            GlFilterCode.SWIRL,
            GlFilterCode.HEXAGON_MOSAIC,
            GlFilterCode.MIRROR,
            GlFilterCode.TRIPLE,
            GlFilterCode.CARTOON,
            GlFilterCode.WATER_REFLECTION
        )
}