package com.shevelev.wizard_camera.core.database.impl.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode

@Entity(tableName = "filter_settings")
data class FilterSettingsDb(
    @PrimaryKey
    @ColumnInfo(name = "filter_settings")
    val id: Long,

    @ColumnInfo(name = "filter", typeAffinity = ColumnInfo.INTEGER)
    val code: GlFilterCode,

    @ColumnInfo(name = "settings")
    val settings: String
)