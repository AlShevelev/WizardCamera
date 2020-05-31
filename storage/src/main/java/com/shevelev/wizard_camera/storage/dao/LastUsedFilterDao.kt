package com.shevelev.wizard_camera.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.shevelev.wizard_camera.storage.entities.LastUsedFilterDb

@Dao
interface LastUsedFilterDao {
    @Insert
    fun create(lastUsedFilter: LastUsedFilterDb)

    @Update
    fun update(lastUsedFilter: LastUsedFilterDb)

    @Query("select * from last_used_filter")
    fun read(): List<LastUsedFilterDb>
}