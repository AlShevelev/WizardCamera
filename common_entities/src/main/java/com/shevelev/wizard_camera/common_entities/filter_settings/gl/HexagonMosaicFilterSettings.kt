package com.shevelev.wizard_camera.common_entities.filter_settings.gl

import android.os.Parcelable
import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.common_entities.enums.Size
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HexagonMosaicFilterSettings(
    override val code: GlFilterCode = GlFilterCode.HEXAGON_MOSAIC,
    val size: Size
): GlFilterSettings, Parcelable