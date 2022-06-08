package com.shevelev.wizard_camera.feature.filters_facade.impl.settings

import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings

interface FilterSettingsFacade {
    suspend fun init()

    operator fun get(code: GlFilterCode): GlFilterSettings

    suspend fun update(settings: GlFilterSettings)
}