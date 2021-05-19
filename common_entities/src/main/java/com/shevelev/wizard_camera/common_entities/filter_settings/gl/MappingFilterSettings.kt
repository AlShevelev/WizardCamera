package com.shevelev.wizard_camera.common_entities.filter_settings.gl

import android.os.Parcelable
import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.common_entities.enums.MappingFilterTexture
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MappingFilterSettings(
    override val code: GlFilterCode = GlFilterCode.MAPPING,
    val texture: MappingFilterTexture,

    // From 5(included) to 20(included)
    val mixFactor: Int
): GlFilterSettings, Parcelable