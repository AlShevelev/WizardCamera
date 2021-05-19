package com.shevelev.wizard_camera.common_entities.filter_settings.gl

import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings

data class EmptyFilterSettings(
    override val code: GlFilterCode
): GlFilterSettings