package com.shevelev.wizard_camera.common_entities.filter_settings.gl

import android.os.Parcelable
import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewspaperFilterSettings(
    override val code: GlFilterCode = GlFilterCode.NEWSPAPER,
    val isGrayscale: Boolean
): GlFilterSettings, Parcelable