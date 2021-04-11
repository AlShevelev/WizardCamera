package com.shevelev.wizard_camera.common_entities.filter_settings

import android.os.Parcelable
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TripleFilterSettings(
    override val code: FilterCode = FilterCode.TRIPLE,
    val isHorizontal: Boolean
): FilterSettings, Parcelable