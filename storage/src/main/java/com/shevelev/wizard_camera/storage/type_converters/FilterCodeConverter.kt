package com.shevelev.wizard_camera.storage.type_converters

import androidx.room.TypeConverter
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import java.lang.UnsupportedOperationException

class FilterCodeConverter {
    @TypeConverter
    fun toDb(sourceData: FilterCode?): Int? = sourceData?.value

    @TypeConverter
    fun fromDb(sourceData: Int?): FilterCode? =
        sourceData?.let {
            FilterCode.values().first { code -> code.value == it }
        }
}