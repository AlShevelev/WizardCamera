package com.shevelev.wizard_camera.common_entities.entities

import android.os.Parcelable
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.ZonedDateTime

/**
 * One photo shot
 */
@Parcelize
data class PhotoShot (
    val id: Long,
    val fileName: String,
    val created: ZonedDateTime,
    val filter: FilterCode
) :Parcelable