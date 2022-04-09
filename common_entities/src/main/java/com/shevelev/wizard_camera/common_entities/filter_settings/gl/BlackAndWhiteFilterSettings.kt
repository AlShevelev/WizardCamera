package com.shevelev.wizard_camera.common_entities.filter_settings.gl

import android.os.Parcelable
import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BlackAndWhiteFilterSettings(
    override val code: GlFilterCode = GlFilterCode.BLACK_AND_WHITE,
    val isInverted: Boolean
): GlFilterSettings, Parcelable