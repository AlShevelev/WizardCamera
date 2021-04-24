package com.shevelev.wizard_camera.application.di

import com.shevelev.wizard_camera.camera.settings_repository.CameraSettingsRepository
import com.shevelev.wizard_camera.camera.settings_repository.CameraSettingsRepositoryImpl
import com.shevelev.wizard_camera.common_entities.di_scopes.ApplicationScope
import com.shevelev.wizard_camera.shared.crashlytics.CrashlyticsFacadeImpl
import com.shevelev.wizard_camera.utils.device_info.DeviceInfoProvider
import com.shevelev.wizard_camera.utils.device_info.DeviceInfoProviderImpl
import com.shevelev.wizard_camera.shared.files.FilesHelper
import com.shevelev.wizard_camera.shared.files.FilesHelperImpl
import com.shevelev.wizard_camera.shared.media_scanner.MediaScanner
import com.shevelev.wizard_camera.shared.media_scanner.MediaScannerImpl
import com.shevelev.wizard_camera.storage.repositories.*
import com.shevelev.wizard_camera.storage.type_converters.filter_settings.FilerSettingsConverter
import com.shevelev.wizard_camera.storage.type_converters.filter_settings.FilerSettingsConverterImpl
import com.shevelev.wizard_camera.utils.crashlytics.CrashlyticsFacade
import dagger.Binds
import dagger.Module

@Module
abstract class AppModuleBinds {
    @Binds
    abstract fun provideFilesHelper(helper: FilesHelperImpl): FilesHelper

    @Binds
    abstract fun provideMediaScanner(scanner: MediaScannerImpl): MediaScanner

    @Binds
    abstract fun provideFilerSettingsConverter(converter: FilerSettingsConverterImpl): FilerSettingsConverter

    @Binds
    abstract fun providePhotoShotRepository(repository: PhotoShotRepositoryImpl): PhotoShotRepository

    @Binds
    abstract fun provideLastUsedFilterRepository(repository: LastUsedFilterRepositoryImpl): LastUsedFilterRepository

    @Binds
    abstract fun provideFavoriteFilterRepository(repository: FavoriteFilterRepositoryImpl): FavoriteFilterRepository

    @Binds
    abstract fun provideFilterSettingsRepository(repository: FilterSettingsRepositoryImpl): FilterSettingsRepository

    @Binds
    abstract fun provideDeviceInfoProviderImpl(provider: DeviceInfoProviderImpl): DeviceInfoProvider

    @Binds
    abstract fun provideCrashlyticsFacade(facade: CrashlyticsFacadeImpl): CrashlyticsFacade

    @Binds
    @ApplicationScope
    abstract fun provideCameraSettingsRepository(repository: CameraSettingsRepositoryImpl): CameraSettingsRepository
}