package com.shevelev.wizard_camera.storage.repositories

import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings

interface FilterSettingsRepository {
    fun read(): List<FilterSettings>

    fun update(settings: FilterSettings)
}