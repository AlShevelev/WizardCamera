package com.shevelev.wizard_camera.core.database.impl.core

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shevelev.wizard_camera.core.database.impl.dao.FavoriteFilterDao
import com.shevelev.wizard_camera.core.database.impl.dao.FilterSettingsDao
import com.shevelev.wizard_camera.core.database.impl.dao.LastUsedFilterDao
import com.shevelev.wizard_camera.core.database.impl.dao.PhotoShotDao
import com.shevelev.wizard_camera.core.database.impl.entities.FavoriteFilterDb
import com.shevelev.wizard_camera.core.database.impl.entities.FilterSettingsDb
import com.shevelev.wizard_camera.core.database.impl.entities.LastUsedFilterDb
import com.shevelev.wizard_camera.core.database.impl.entities.PhotoShotDb
import com.shevelev.wizard_camera.core.database.impl.type_converters.DateTimeTypeConverter
import com.shevelev.wizard_camera.core.database.impl.type_converters.FilterCodeConverter

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