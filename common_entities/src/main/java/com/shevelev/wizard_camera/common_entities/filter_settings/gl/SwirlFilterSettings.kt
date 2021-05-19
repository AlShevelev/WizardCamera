package com.shevelev.wizard_camera.common_entities.filter_settings.gl

import android.os.Parcelable
import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SwirlFilterSettings(
    override val code: GlFilterCode = GlFilterCode.SWIRL,

    /**
     * Rotation factor (from 1(included) to 10(included))
     */
    val rotation: Int,

    /**
     * Radius factor (from 1(included) to 10(included))
     */
    val radius: Int,

    val invertRotation: Boolean
): GlFilterSettings, Parcelable