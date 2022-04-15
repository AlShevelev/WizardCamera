package com.shevelev.wizard_camera.core.database.impl.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import org.threeten.bp.ZonedDateTime

@Entity(tableName = "photo_shot")
data class PhotoShotDb(
    @PrimaryKey
    @ColumnInfo(name = "photo_shot_id")
    val id: Long,

    @ColumnInfo(name = "file_name")
    val fileName: String,

    @ColumnInfo(name = "created", typeAffinity = ColumnInfo.BLOB)
    val created: ZonedDateTime,

    @ColumnInfo(name = "created_sort", index = true)
    val createdSort: Long,

    @ColumnInfo(name = "filter_code", typeAffinity = ColumnInfo.INTEGER)
    val filterCode: GlFilterCode,

    @ColumnInfo(name = "filter_settings")
    val filterSettings: String,

    @ColumnInfo(name = "content_uri")
    val contentUri: String?
)
