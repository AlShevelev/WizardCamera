package com.shevelev.wizard_camera.capturing_service

import android.os.Parcelable
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.parceler.GlFilterSettingsParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler

@Parcelize
@TypeParceler<GlFilterSettings, GlFilterSettingsParceler>
internal data class PhotoShotCaptureCompleteParams(
     val key: Long,
     val filter: GlFilterSettings
) : Parcelable