package com.shevelev.wizard_camera.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.shevelev.wizard_camera.storage.entities.PhotoShotDb

@Dao
interface PhotoShotDao {
    @Insert
    fun create(shot: PhotoShotDb)

    @Query("select * from photo_shot order by created_sort desc limit :limit offset :offset")
    fun readPaged(limit: Int, offset: Int): List<PhotoShotDb>

    @Query("delete from photo_shot where photo_shot_id = :id")
    fun deleteById(id: Long)
}