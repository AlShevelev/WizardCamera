package com.shevelev.wizard_camera.core.database.impl.type_converters

import androidx.room.TypeConverter
import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup

internal class FiltersGroupConverter {
    @TypeConverter
    fun toDb(sourceData: FiltersGroup?): Int? = sourceData?.index

    @TypeConverter
    fun fromDb(sourceData: Int?): FiltersGroup? =
        sourceData?.let {
            FiltersGroup.fromIndex(it)
        }
}