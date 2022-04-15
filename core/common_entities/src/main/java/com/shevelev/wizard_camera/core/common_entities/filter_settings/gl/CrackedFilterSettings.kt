package com.shevelev.wizard_camera.core.common_entities.filter_settings.gl

import android.os.Parcelable
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import kotlinx.parcelize.Parcelize

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