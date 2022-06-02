package com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.groups

import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.last_used_filters.LastUsedFilters
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.settings.FilterSettingsFacade
import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl.FilterDisplayDataList

internal class ColorsFilterGroupStorage(
    favoriteFilters: FiltersGroupStorage,
    displayData: FilterDisplayDataList,
    filterSettings: FilterSettingsFacade,
    lastUsedFilters: LastUsedFilters
) : FiltersGroupStorageBase(
    favoriteFilters,
    displayData,
    filterSettings,
    lastUsedFilters,
    FiltersGroup.COLORS
),  FiltersGroupStorage {

    override fun getSupportedFilters(): List<GlFilterCode> =
        listOf(
            GlFilterCode.BLUE_ORANGE,
            GlFilterCode.CONTRAST,
            GlFilterCode.BLACK_AND_WHITE,
            GlFilterCode.GRAY,
            GlFilterCode.NEGATIVE,
            GlFilterCode.NOSTALGIA,
            GlFilterCode.CASTING,
            GlFilterCode.CARTOON,
        )
}