package com.shevelev.wizard_camera.core.database.impl.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode

@Entity(tableName = "last_used_filter")
internal data class LastUsedFilterDb(
    @PrimaryKey
    @ColumnInfo(name = "last_used_filter_id")
    val id: Long,

    @ColumnInfo(name = "filter", typeAffinity = ColumnInfo.INTEGER)
    val code: GlFilterCode,

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean
)