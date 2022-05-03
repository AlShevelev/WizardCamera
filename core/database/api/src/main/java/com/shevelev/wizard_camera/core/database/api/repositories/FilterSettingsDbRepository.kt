package com.shevelev.wizard_camera.core.database.api.repositories

import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings

interface FilterSettingsDbRepository {
    fun read(): List<GlFilterSettings>

    fun update(settings: GlFilterSettings)
}