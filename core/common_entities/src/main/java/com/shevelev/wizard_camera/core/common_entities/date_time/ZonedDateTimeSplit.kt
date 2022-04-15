package com.shevelev.wizard_camera.core.common_entities.date_time

data class ZonedDateTimeSplit(
    val seconds: Long,
    val nanoseconds: Int,
    val timeZoneId: String
)