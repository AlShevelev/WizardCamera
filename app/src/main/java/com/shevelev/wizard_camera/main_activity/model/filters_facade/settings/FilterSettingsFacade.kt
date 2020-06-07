package com.shevelev.wizard_camera.main_activity.model.filters_facade.settings

import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings

interface FilterSettingsFacade {
    suspend fun init()

    operator fun get(code: FilterCode): FilterSettings

    suspend fun update(settings: FilterSettings)
}