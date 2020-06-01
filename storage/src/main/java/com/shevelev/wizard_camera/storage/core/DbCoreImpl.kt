package com.shevelev.wizard_camera.storage.core

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shevelev.wizard_camera.storage.dao.LastUsedFilterDao
import com.shevelev.wizard_camera.storage.dao.PhotoShotDao
import com.shevelev.wizard_camera.storage.entities.FavoriteFilterDb
import com.shevelev.wizard_camera.storage.entities.LastUsedFilterDb
import com.shevelev.wizard_camera.storage.entities.PhotoShotDb
import com.shevelev.wizard_camera.storage.type_converters.DateTimeTypeConverter
import com.shevelev.wizard_camera.storage.type_converters.FilterCodeConverter

@Database(
    entities = [
        PhotoShotDb::class,
        LastUsedFilterDb::class,
        FavoriteFilterDb::class
    ],
    version = 1)
@TypeConverters(
    DateTimeTypeConverter::class,
    FilterCodeConverter::class)
abstract class DbCoreImpl: RoomDatabase(), DbCore {
    abstract override val photoShot: PhotoShotDao
    abstract override val lastUsedFilter: LastUsedFilterDao

    /**
     * Run some code in transaction
     */
    override fun <T>runConsistent(action: () -> T): T =
        runInTransaction<T> {
            action()
        }
}