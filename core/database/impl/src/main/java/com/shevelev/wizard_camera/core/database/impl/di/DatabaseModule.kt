package com.shevelev.wizard_camera.core.database.impl.di

import androidx.room.Room
import com.shevelev.wizard_camera.core.database.api.repositories.FavoriteFilterDbRepository
import com.shevelev.wizard_camera.core.database.api.repositories.FilterSettingsDbRepository
import com.shevelev.wizard_camera.core.database.api.repositories.LastUsedFilterDbRepository
import com.shevelev.wizard_camera.core.database.api.repositories.PhotoShotDbRepository
import com.shevelev.wizard_camera.core.database.impl.core.DbCore
import com.shevelev.wizard_camera.core.database.impl.core.DbCoreImpl
import com.shevelev.wizard_camera.core.database.impl.repositories.FavoriteFilterDbRepositoryImpl
import com.shevelev.wizard_camera.core.database.impl.repositories.FilterSettingsDbRepositoryImpl
import com.shevelev.wizard_camera.core.database.impl.repositories.LastUsedFilterDbRepositoryImpl
import com.shevelev.wizard_camera.core.database.impl.repositories.PhotoShotDbRepositoryImpl
import com.shevelev.wizard_camera.core.database.impl.type_converters.filter_settings.FilerSettingsConverter
import com.shevelev.wizard_camera.core.database.impl.type_converters.filter_settings.FilerSettingsConverterImpl
import org.koin.dsl.module

val DatabaseModule = module(createdAtStart = false) {
    single<DbCore> {
        Room
        .databaseBuilder(
            get(),
            DbCoreImpl::class.java,
            "wizard_camera.db"
        )
        .build()
    }

    factory<FilerSettingsConverter> {
        FilerSettingsConverterImpl(
            moshi = get()
        )
    }

    factory<FavoriteFilterDbRepository> {
        FavoriteFilterDbRepositoryImpl(
            db = get()
        )
    }

    factory<FilterSettingsDbRepository> {
        FilterSettingsDbRepositoryImpl(
            db = get(),
            filerSettingsConverter = get()
        )
    }

    factory<LastUsedFilterDbRepository> {
        LastUsedFilterDbRepositoryImpl(
            db = get()
        )
    }

    factory<PhotoShotDbRepository> {
        PhotoShotDbRepositoryImpl(
            db = get(),
            filerSettingsConverter = get()
        )
    }
}