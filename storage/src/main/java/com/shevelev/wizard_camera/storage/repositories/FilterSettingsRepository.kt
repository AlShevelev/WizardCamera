package com.shevelev.wizard_camera.storage.repositories

import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings

interface FilterSettingsRepository {
    fun read(): List<GlFilterSettings>

    fun update(settings: GlFilterSettings)
}