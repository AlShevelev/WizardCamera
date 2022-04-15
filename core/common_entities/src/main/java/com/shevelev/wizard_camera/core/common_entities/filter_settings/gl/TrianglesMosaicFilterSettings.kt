package com.shevelev.wizard_camera.core.common_entities.filter_settings.gl

import android.os.Parcelable
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.enums.Size
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrianglesMosaicFilterSettings(
    override val code: GlFilterCode = GlFilterCode.TRIANGLES_MOSAIC,
    val size: Size
): GlFilterSettings, Parcelable