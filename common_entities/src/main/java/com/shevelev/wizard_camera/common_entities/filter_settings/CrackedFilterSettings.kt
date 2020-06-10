package com.shevelev.wizard_camera.common_entities.filter_settings

import com.shevelev.wizard_camera.common_entities.enums.FilterCode

data class CrackedFilterSettings(
    override val code: FilterCode = FilterCode.CRACKED,

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
): FilterSettings