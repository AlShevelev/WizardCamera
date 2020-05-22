package com.shevelev.wizard_camera.storage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
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

    @ColumnInfo(name = "filter", typeAffinity = ColumnInfo.INTEGER)
    val filter: FilterCode
)
