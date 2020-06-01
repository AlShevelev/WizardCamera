package com.shevelev.wizard_camera.application.di

import com.shevelev.wizard_camera.shared.files.FilesHelper
import com.shevelev.wizard_camera.shared.files.FilesHelperImpl
import com.shevelev.wizard_camera.shared.media_scanner.MediaScanner
import com.shevelev.wizard_camera.shared.media_scanner.MediaScannerImpl
import com.shevelev.wizard_camera.storage.repositories.*
import dagger.Binds
import dagger.Module

@Module
abstract class AppModuleBinds {
    @Binds
    abstract fun provideFilesHelper(helper: FilesHelperImpl): FilesHelper

    @Binds
    abstract fun provideMediaScanner(scanner: MediaScannerImpl): MediaScanner

    @Binds
    abstract fun providePhotoShotRepository(repository: PhotoShotRepositoryImpl): PhotoShotRepository

    @Binds
    abstract fun provideLastUsedFilterRepository(repository: LastUsedFilterRepositoryImpl): LastUsedFilterRepository

    @Binds
    abstract fun provideFavoriteFilterRepository(repository: FavoriteFilterRepositoryImpl): FavoriteFilterRepository
}