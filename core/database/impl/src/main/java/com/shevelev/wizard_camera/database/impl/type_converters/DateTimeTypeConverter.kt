package com.shevelev.wizard_camera.database.impl.type_converters

import androidx.room.TypeConverter
import com.shevelev.wizard_camera.common_entities.date_time.ZonedDateTimeSplit
import com.shevelev.wizard_camera.utils.date_time.toSplit
import com.shevelev.wizard_camera.utils.date_time.toZoneDateTime
import org.threeten.bp.ZonedDateTime
import java.nio.ByteBuffer

class DateTimeTypeConverter {
    @TypeConverter
    fun toDb(sourceData: ZonedDateTime?): ByteArray? =
        sourceData?.let {
            val splitSource = it.toSplit()

            val timeZoneAsBytes = splitSource.timeZoneId.toByteArray(Charsets.UTF_8)

            return ByteBuffer.allocate(12+timeZoneAsBytes.size)
                .putLong(splitSource.seconds)
                .putInt(splitSource.nanoseconds)
                .put(timeZoneAsBytes)
                .array()
        }

    @TypeConverter
    fun fromDb(sourceData: ByteArray?): ZonedDateTime? =
        sourceData?.let {
            val buffer = ByteBuffer.wrap(sourceData)

            val seconds = buffer.getLong(0)
            val nanoSeconds = buffer.getInt(8)

            val timeZoneAsBytes = ByteArray(sourceData.size - 12)

            for(i in 12 until sourceData.size) {
                timeZoneAsBytes[i-12] = buffer.get(i)
            }

            ZonedDateTimeSplit(seconds, nanoSeconds, timeZoneAsBytes.toString(Charsets.UTF_8)).toZoneDateTime()
        }
}