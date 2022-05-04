package com.shevelev.wizard_camera.core.photo_files.impl.di

import com.shevelev.wizard_camera.core.photo_files.impl.MediaScanner
import com.shevelev.wizard_camera.core.photo_files.api.new.PhotoShotRepository
import com.shevelev.wizard_camera.core.photo_files.impl.MediaScannerImpl
import com.shevelev.wizard_camera.core.photo_files.impl.new.conventional.ConventionalFilesRepository
import org.koin.dsl.module

val PhotoFilesModule = module(createdAtStart = false) {
    factory<MediaScanner> {
        MediaScannerImpl(
            appContext = get()
        )
    }

    // must be single!
    single<PhotoShotRepository> {
        ConventionalFilesRepository(
            appContext = get(),
            appInfo = get(),
            mediaScanner = get(),
            bitmapOrientationCorrector = get(),
            bitmapHelper = get(),
            photoShotRepository = get()
        )
    }
}