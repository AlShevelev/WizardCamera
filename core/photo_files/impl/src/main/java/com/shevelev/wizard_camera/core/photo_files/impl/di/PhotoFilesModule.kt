package com.shevelev.wizard_camera.core.photo_files.impl.di

import com.shevelev.wizard_camera.core.build_info.api.BuildInfo
import com.shevelev.wizard_camera.core.photo_files.api.FilesHelper
import com.shevelev.wizard_camera.core.photo_files.api.MediaScanner
import com.shevelev.wizard_camera.core.photo_files.impl.FilesHelperImpl
import com.shevelev.wizard_camera.core.photo_files.impl.MediaScannerImpl
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
}