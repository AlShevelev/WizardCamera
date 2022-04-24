package com.shevelev.wizard_camera.core.database.impl.type_converters

import androidx.room.TypeConverter
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode

internal class FilterCodeConverter {
    @TypeConverter
    fun toDb(sourceData: GlFilterCode?): Int? = sourceData?.value

    @TypeConverter
    fun fromDb(sourceData: Int?): GlFilterCode? =
        sourceData?.let {
            GlFilterCode.values().first { code -> code.value == it }
        }
}