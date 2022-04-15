package com.shevelev.wizard_camera.core.database.impl.core

import com.shevelev.wizard_camera.core.database.impl.dao.FavoriteFilterDao
import com.shevelev.wizard_camera.core.database.impl.dao.FilterSettingsDao
import com.shevelev.wizard_camera.core.database.impl.dao.LastUsedFilterDao
import com.shevelev.wizard_camera.core.database.impl.dao.PhotoShotDao

interface DbCore {
    val photoShot: PhotoShotDao
    val lastUsedFilter: LastUsedFilterDao
    val favoriteFilter: FavoriteFilterDao
    val filterSettings: FilterSettingsDao

    fun <T> runConsistent(action: () -> T): T
}