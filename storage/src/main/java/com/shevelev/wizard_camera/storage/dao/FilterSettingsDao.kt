package com.shevelev.wizard_camera.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.storage.entities.FilterSettingsDb

@Dao
interface FilterSettingsDao {
    @Insert
    fun create(settings: FilterSettingsDb)

    @Query("select * from filter_settings where filter = :code limit 1")
    fun read(code: GlFilterCode): FilterSettingsDb?

    @Query("select * from filter_settings")
    fun read(): List<FilterSettingsDb>

    @Update
    fun update(settings: FilterSettingsDb)
}