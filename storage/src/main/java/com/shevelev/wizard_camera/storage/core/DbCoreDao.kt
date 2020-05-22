package com.shevelev.wizard_camera.storage.core

import com.shevelev.wizard_camera.storage.dao.PhotoShotDao

interface DbCoreDao {
    val photoShot: PhotoShotDao
}