package com.shevelev.wizard_camera.utils.date_time

import com.shevelev.wizard_camera.common_entities.dto.ZonedDateTimeSplit
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

fun ZonedDateTime.toSplit(): ZonedDateTimeSplit {
    val zone = this.zone.id

    val nowInstant = this.toInstant()

    val seconds = nowInstant.epochSecond
    val nanoseconds = nowInstant.nano

    return ZonedDateTimeSplit(seconds, nanoseconds, zone)
}

fun ZonedDateTimeSplit.toZoneDateTime(): ZonedDateTime {
    val restoredInstant = Instant.ofEpochSecond(seconds, nanoseconds.toLong())
    return ZonedDateTime.ofInstant(restoredInstant, ZoneId.of(this.timeZoneId))
}