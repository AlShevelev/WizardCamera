package com.shevelev.wizard_camera.common_entities.dto

data class ZonedDateTimeSplit(
    val seconds: Long,
    val nanoseconds: Int,
    val timeZoneId: String
)