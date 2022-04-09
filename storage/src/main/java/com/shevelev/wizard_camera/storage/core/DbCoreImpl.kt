package com.shevelev.wizard_camera.storage.core

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shevelev.wizard_camera.storage.dao.FavoriteFilterDao
import com.shevelev.wizard_camera.storage.dao.FilterSettingsDao
import com.shevelev.wizard_camera.storage.dao.LastUsedFilterDao
import com.shevelev.wizard_camera.storage.dao.PhotoShotDao
import com.shevelev.wizard_camera.storage.entities.FavoriteFilterDb
import com.shevelev.wizard_camera.storage.entities.FilterSettingsDb
import com.shevelev.wizard_camera.storage.entities.LastUsedFilterDb
import com.shevelev.wizard_camera.storage.entities.PhotoShotDb
import com.shevelev.wizard_camera.storage.type_converters.DateTimeTypeConverter
import com.shevelev.wizard_camera.storage.type_converters.FilterCodeConverter

@Database(
    entities = [
        PhotoShotDb::class,
        LastUsedFilterDb::class,
        FavoriteFilterDb::class,
        FilterSettingsDb::class
    ],
    version = 1)
@TypeConverters(
    DateTimeTypeConverter::class,
    FilterCodeConverter::class)
abstract class DbCoreImpl: RoomDatabase(), DbCore {
    abstract override val photoShot: PhotoShotDao
    abstract override val lastUsedFilter: LastUsedFilterDao
    abstract override val favoriteFilter: FavoriteFilterDao
    abstract override val filterSettings: FilterSettingsDao

    /**
     * Run some code in transaction
     */
    override fun <T>runConsistent(action: () -> T): T =
        runInTransaction<T> {
            action()
        }
}