package com.shevelev.wizard_camera.core.common_entities.filter_settings.gl

import android.os.Parcelable
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import kotlinx.parcelize.Parcelize

@Parcelize
data class TripleFilterSettings(
    override val code: GlFilterCode = GlFilterCode.TRIPLE,
    val isHorizontal: Boolean
): GlFilterSettings, Parcelable