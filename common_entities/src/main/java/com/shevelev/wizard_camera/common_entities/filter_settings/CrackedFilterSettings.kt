package com.shevelev.wizard_camera.common_entities.filter_settings

import com.shevelev.wizard_camera.common_entities.enums.FilterCode

data class CrackedFilterSettings(
    override val code: FilterCode = FilterCode.CRACKED,
    val shards: Int,
    val randomA: Float,
    val randomB: Float,
    val randomC: Float
): FilterSettings