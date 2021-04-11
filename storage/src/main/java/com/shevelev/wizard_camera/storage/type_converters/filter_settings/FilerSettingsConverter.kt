package com.shevelev.wizard_camera.storage.type_converters.filter_settings

import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings

interface FilerSettingsConverter {
    fun toString(settings: FilterSettings): String

    fun fromString(code: FilterCode, settings: String): FilterSettings
}