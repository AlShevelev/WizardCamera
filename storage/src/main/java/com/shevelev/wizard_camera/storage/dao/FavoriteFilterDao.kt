package com.shevelev.wizard_camera.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.storage.entities.FavoriteFilterDb

@Dao
interface FavoriteFilterDao {
    @Insert
    fun create(filter: FavoriteFilterDb)

    @Query("select * from favorite_filter")
    fun read(): List<FavoriteFilterDb>

    @Query("select 1 from favorite_filter where filter = :filterCode limit 1")
    fun exists(filterCode: GlFilterCode): Int?

    @Query("delete from favorite_filter where filter = :filterCode")
    fun delete(filterCode: GlFilterCode)
}