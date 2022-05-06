package com.shevelev.wizard_camera.core.photo_files.impl.di

import android.os.Build
import com.shevelev.wizard_camera.core.photo_files.impl.photo_shot_repository.conventional.media_scanner.MediaScanner
import com.shevelev.wizard_camera.core.photo_files.api.photo_shot_repository.PhotoShotRepository
import com.shevelev.wizard_camera.core.photo_files.impl.photo_shot_repository.conventional.media_scanner.MediaScannerImpl
import com.shevelev.wizard_camera.core.photo_files.impl.photo_shot_repository.conventional.ConventionalFilesRepository
import com.shevelev.wizard_camera.core.photo_files.impl.photo_shot_repository.media_store.MediaStoreFilesRepository
import org.koin.dsl.module

val PhotoFilesModule = module(createdAtStart = false) {
    factory<MediaScanner> {
        MediaScannerImpl(
            appContext = get()
        )
    }

    // must be single!
    single<PhotoShotRepository> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStoreFilesRepository(
                appContext = get(),
                appInfo = get(),
                bitmapOrientationCorrector = get(),
                bitmapHelper = get(),
                photoShotRepository = get()
            )
        } else {
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
}