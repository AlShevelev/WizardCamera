package com.shevelev.wizard_camera.storage.repositories

import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.storage.core.DbCore
import com.shevelev.wizard_camera.storage.mapping.map
import javax.inject.Inject

class PhotoShotRepositoryImpl
@Inject
constructor(
    private val db: DbCore
): PhotoShotRepository {
    override fun create(shot: PhotoShot) = db.photoShot.create(shot.map())

    override fun readPaged(limit: Int, offset: Int): List<PhotoShot> = db.photoShot.readPaged(limit, offset).map { it.map() }

    override fun deleteById(id: Long) = db.photoShot.deleteById(id)
}