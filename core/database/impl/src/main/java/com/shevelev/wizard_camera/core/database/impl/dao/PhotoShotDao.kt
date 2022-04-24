package com.shevelev.wizard_camera.core.database.impl.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.shevelev.wizard_camera.core.database.impl.entities.PhotoShotDb

@Dao
internal interface PhotoShotDao {
    @Insert
    fun create(shot: PhotoShotDb)

    @Update
    fun update(shot: PhotoShotDb)

    @Query("select * from photo_shot order by created_sort desc limit :limit offset :offset")
    fun readPaged(limit: Int, offset: Int): List<PhotoShotDb>

    @Query("delete from photo_shot where photo_shot_id = :id")
    fun deleteById(id: Long)
}