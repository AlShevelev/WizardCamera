package com.shevelev.wizard_camera.common_entities.filter_settings

import android.os.Parcelable
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.common_entities.enums.Size
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HexagonMosaicFilterSettings(
    override val code: FilterCode = FilterCode.HEXAGON_MOSAIC,
    val size: Size
): FilterSettings, Parcelable