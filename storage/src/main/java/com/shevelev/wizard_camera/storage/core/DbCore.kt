package com.shevelev.wizard_camera.storage.core

import com.shevelev.wizard_camera.storage.dao.FavoriteFilterDao
import com.shevelev.wizard_camera.storage.dao.FilterSettingsDao
import com.shevelev.wizard_camera.storage.dao.LastUsedFilterDao
import com.shevelev.wizard_camera.storage.dao.PhotoShotDao

interface DbCore {
    val photoShot: PhotoShotDao
    val lastUsedFilter: LastUsedFilterDao
    val favoriteFilter: FavoriteFilterDao
    val filterSettings: FilterSettingsDao

    fun <T> runConsistent(action: () -> T): T
}