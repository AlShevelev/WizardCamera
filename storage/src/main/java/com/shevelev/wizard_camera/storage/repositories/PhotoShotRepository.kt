package com.shevelev.wizard_camera.storage.repositories

import com.shevelev.wizard_camera.common_entities.entities.PhotoShot

interface PhotoShotRepository {
    fun create(shot: PhotoShot)

    fun update(shot: PhotoShot)

    fun readPaged(limit: Int, offset: Int): List<PhotoShot>

    fun deleteById(id: Long)
}