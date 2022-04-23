package com.shevelev.wizard_camera.core.database.impl.di

import androidx.room.Room
import com.shevelev.wizard_camera.core.database.api.repositories.FavoriteFilterRepository
import com.shevelev.wizard_camera.core.database.api.repositories.FilterSettingsRepository
import com.shevelev.wizard_camera.core.database.api.repositories.LastUsedFilterRepository
import com.shevelev.wizard_camera.core.database.api.repositories.PhotoShotRepository
import com.shevelev.wizard_camera.core.database.impl.core.DbCore
import com.shevelev.wizard_camera.core.database.impl.core.DbCoreImpl
import com.shevelev.wizard_camera.core.database.impl.repositories.FavoriteFilterRepositoryImpl
import com.shevelev.wizard_camera.core.database.impl.repositories.FilterSettingsRepositoryImpl
import com.shevelev.wizard_camera.core.database.impl.repositories.LastUsedFilterRepositoryImpl
import com.shevelev.wizard_camera.core.database.impl.repositories.PhotoShotRepositoryImpl
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

    factory<FavoriteFilterRepository> {
        FavoriteFilterRepositoryImpl(
            db = get()
        )
    }

    factory<FilterSettingsRepository> {
        FilterSettingsRepositoryImpl(
            db = get(),
            filerSettingsConverter = get()
        )
    }

    factory<LastUsedFilterRepository> {
        LastUsedFilterRepositoryImpl(
            db = get()
        )
    }

    factory<PhotoShotRepository> {
        PhotoShotRepositoryImpl(
            db = get(),
            filerSettingsConverter = get()
        )
    }
}