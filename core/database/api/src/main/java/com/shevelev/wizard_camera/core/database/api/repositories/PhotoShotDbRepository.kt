package com.shevelev.wizard_camera.core.database.api.repositories

import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot

interface PhotoShotDbRepository {
    fun create(shot: PhotoShot)

    fun update(shot: PhotoShot)

    fun readPaged(limit: Int, offset: Int): List<PhotoShot>

    fun deleteById(id: Long)
}