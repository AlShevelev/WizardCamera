package com.shevelev.wizard_camera.common_entities.filter_settings

import android.os.Parcelable
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SwirlFilterSettings(
    override val code: FilterCode = FilterCode.SWIRL,

    /**
     * Rotation factor (from 1(included) to 10(included))
     */
    val rotation: Int,

    /**
     * Radius factor (from 1(included) to 10(included))
     */
    val radius: Int,

    val invertRotation: Boolean
): FilterSettings, Parcelable