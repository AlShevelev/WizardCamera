package com.shevelev.wizard_camera.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import com.shevelev.wizard_camera.storage.entities.PhotoShotDb

@Dao
interface PhotoShotDao {
    @Insert
    fun insert(shot: PhotoShotDb)
}