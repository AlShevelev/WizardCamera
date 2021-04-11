package com.shevelev.wizard_camera.common_entities.entities

import android.net.Uri
import android.os.Parcelable
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.parceler.FilterSettingsParceler
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.TypeParceler
import org.threeten.bp.ZonedDateTime

/**
 * One photo shot
 */
@Parcelize
@TypeParceler<FilterSettings, FilterSettingsParceler>
data class PhotoShot (
    val id: Long,
    val fileName: String,
    val created: ZonedDateTime,
    val filter: FilterSettings,
    val contentUri: Uri?
) : Parcelable