package com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.settings

import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings

internal interface FilterSettingsFacade {
    suspend fun init()

    operator fun get(code: GlFilterCode): GlFilterSettings

    suspend fun update(settings: GlFilterSettings)
}