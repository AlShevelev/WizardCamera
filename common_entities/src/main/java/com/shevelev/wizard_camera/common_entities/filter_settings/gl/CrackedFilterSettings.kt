package com.shevelev.wizard_camera.common_entities.filter_settings.gl

import android.os.Parcelable
import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CrackedFilterSettings(
    override val code: GlFilterCode = GlFilterCode.CRACKED,

    /**
     * Shards quantity (from 3(included) to 30(included))
     */
    val shards: Int,

    /**
     * Shard shape random values (from 1.0(included) to 358.0(included))
     */
    val randomA: Float,
    val randomB: Float,
    val randomC: Float
): GlFilterSettings, Parcelable