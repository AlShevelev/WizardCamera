package com.shevelev.wizard_camera.core.camera_gl.impl.di

import com.shevelev.wizard_camera.core.camera_gl.api.CameraSettingsRepository
import com.shevelev.wizard_camera.core.camera_gl.impl.camera.settings_repository.CameraSettingsRepositoryImpl
import org.koin.dsl.module

val CameraGlModule = module(createdAtStart = false) {
    factory<CameraSettingsRepository> {
        CameraSettingsRepositoryImpl(
            appContext = get()
        )
    }
}
