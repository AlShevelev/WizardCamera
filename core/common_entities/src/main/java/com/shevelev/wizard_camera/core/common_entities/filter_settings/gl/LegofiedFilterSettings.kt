package com.shevelev.wizard_camera.core.common_entities.filter_settings.gl

import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.enums.Size
import kotlinx.parcelize.Parcelize

@Parcelize
data class LegofiedFilterSettings(
    override val code: GlFilterCode = GlFilterCode.LEGOFIED,
    val size: Size
): GlFilterSettings