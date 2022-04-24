package com.shevelev.wizard_camera.core.database.impl.type_converters.filter_settings

import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings

internal interface FilerSettingsConverter {
    fun toString(settings: GlFilterSettings): String

    fun fromString(code: GlFilterCode, settings: String): GlFilterSettings
}