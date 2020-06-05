package com.shevelev.wizard_camera.storage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shevelev.wizard_camera.common_entities.enums.FilterCode

@Entity(tableName = "filter_settings")
data class FilterSettingsDb(
    @PrimaryKey
    @ColumnInfo(name = "filter_settings")
    val id: Long,

    @ColumnInfo(name = "filter", typeAffinity = ColumnInfo.INTEGER)
    val code: FilterCode,

    @ColumnInfo(name = "settings")
    val settings: String
)