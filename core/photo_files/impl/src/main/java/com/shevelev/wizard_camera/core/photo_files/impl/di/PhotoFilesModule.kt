package com.shevelev.wizard_camera.core.photo_files.impl.di

import com.shevelev.wizard_camera.core.build_info.api.BuildInfo
import com.shevelev.wizard_camera.core.photo_files.api.FilesHelper
import com.shevelev.wizard_camera.core.photo_files.api.MediaScanner
import com.shevelev.wizard_camera.core.photo_files.api.new.PhotoFilesRepository
import com.shevelev.wizard_camera.core.photo_files.impl.FilesHelperImpl
import com.shevelev.wizard_camera.core.photo_files.impl.MediaScannerImpl
import com.shevelev.wizard_camera.core.photo_files.impl.new.conventional.ConventionalFilesRepository
import org.koin.dsl.module

val PhotoFilesModule = module(createdAtStart = false) {
    factory<MediaScanner> {
        MediaScannerImpl(
            appContext = get()
        )
    }

    factory<FilesHelper> {
        FilesHelperImpl(
            appContext = get(),
            appName = get<BuildInfo>().appName
        )
    }

    // must be single!
    single<PhotoFilesRepository> {
        ConventionalFilesRepository(
            appContext = get(),
            appName = get<BuildInfo>().appName,
            mediaScanner = get(),
            bitmapOrientationCorrector = get(),
            bitmapHelper = get(),
            photoShotRepository = get()
        )
        // or MediaStoreFilesRepository
    }
}