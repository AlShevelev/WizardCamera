package com.shevelev.wizard_camera.core.common_entities.entities

import android.net.Uri
import android.os.Parcelable
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.parceler.GlFilterSettingsParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import org.threeten.bp.ZonedDateTime

/**
 * One photo shot
 */
@Parcelize
@TypeParceler<GlFilterSettings, GlFilterSettingsParceler>
data class PhotoShot (
    val id: Long,
    val fileContentUri: Uri,
    val fileName: String?,
    val created: ZonedDateTime,
    val filter: GlFilterSettings,
    val mediaContentUri: Uri?
) : Parcelable