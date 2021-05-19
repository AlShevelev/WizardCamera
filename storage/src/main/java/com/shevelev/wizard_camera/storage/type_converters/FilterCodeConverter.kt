package com.shevelev.wizard_camera.storage.type_converters

import androidx.room.TypeConverter
import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode

class FilterCodeConverter {
    @TypeConverter
    fun toDb(sourceData: GlFilterCode?): Int? = sourceData?.value

    @TypeConverter
    fun fromDb(sourceData: Int?): GlFilterCode? =
        sourceData?.let {
            GlFilterCode.values().first { code -> code.value == it }
        }
}