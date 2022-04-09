package com.shevelev.wizard_camera.storage.type_converters.filter_settings

import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings

interface FilerSettingsConverter {
    fun toString(settings: GlFilterSettings): String

    fun fromString(code: GlFilterCode, settings: String): GlFilterSettings
}