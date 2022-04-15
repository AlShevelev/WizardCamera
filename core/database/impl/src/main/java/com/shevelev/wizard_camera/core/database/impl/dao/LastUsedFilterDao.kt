package com.shevelev.wizard_camera.core.database.impl.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.database.impl.entities.LastUsedFilterDb

@Dao
interface LastUsedFilterDao {
    @Insert
    fun create(lastUsedFilter: LastUsedFilterDb)

    @Query("select * from last_used_filter")
    fun read(): List<LastUsedFilterDb>

    @Update
    fun update(lastUsedFilter: LastUsedFilterDb)

    @Query("delete from last_used_filter where filter = :code and is_favorite = :isFavorite")
    fun delete(code: GlFilterCode, isFavorite: Boolean)
}