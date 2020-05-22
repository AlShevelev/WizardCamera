package com.shevelev.wizard_camera.storage.core

import com.shevelev.wizard_camera.storage.dao.PhotoShotDao

interface DbCore {
    val photoShot: PhotoShotDao

    fun <T> runConsistent(action: () -> T): T
}