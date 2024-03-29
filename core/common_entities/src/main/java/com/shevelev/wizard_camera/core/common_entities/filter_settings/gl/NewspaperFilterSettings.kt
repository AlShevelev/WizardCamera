package com.shevelev.wizard_camera.core.common_entities.filter_settings.gl

import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewspaperFilterSettings(
    override val code: GlFilterCode = GlFilterCode.NEWSPAPER,
    val isGrayscale: Boolean
): GlFilterSettings