package com.shevelev.wizard_camera.common_entities.entities

import android.net.Uri
import android.os.Parcelable
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.parceler.GlFilterSettingsParceler
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.TypeParceler
import org.threeten.bp.ZonedDateTime

/**
 * One photo shot
 */
@Parcelize
@TypeParceler<GlFilterSettings, GlFilterSettingsParceler>
data class PhotoShot (
    val id: Long,
    val fileName: String,
    val created: ZonedDateTime,
    val filter: GlFilterSettings,
    val contentUri: Uri?
) : Parcelable