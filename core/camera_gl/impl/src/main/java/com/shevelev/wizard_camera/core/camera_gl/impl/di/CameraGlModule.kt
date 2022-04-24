package com.shevelev.wizard_camera.core.camera_gl.impl.di

import com.shevelev.wizard_camera.core.camera_gl.api.camera.manager.CameraManager
import com.shevelev.wizard_camera.core.camera_gl.api.camera.renderer.GlRenderer
import com.shevelev.wizard_camera.core.camera_gl.api.shared.factory.GlShaderFiltersFactory
import com.shevelev.wizard_camera.core.camera_gl.impl.camera.manager.CameraManagerImpl
import com.shevelev.wizard_camera.core.camera_gl.impl.camera.renderer.GlRendererImpl
import com.shevelev.wizard_camera.core.camera_gl.impl.camera.settings_repository.CameraSettingsRepository
import com.shevelev.wizard_camera.core.camera_gl.impl.camera.settings_repository.CameraSettingsRepositoryImpl
import com.shevelev.wizard_camera.core.camera_gl.impl.shared.factory.GlShaderFiltersFactoryImpl
import org.koin.dsl.module

val CameraGlModule = module(createdAtStart = false) {
    factory<CameraSettingsRepository> {
        CameraSettingsRepositoryImpl(
            appContext = get()
        )
    }

    factory<CameraManager> {
        CameraManagerImpl(
            cameraSettingsRepository = get()
        )
    }

    factory<GlShaderFiltersFactory> {
        GlShaderFiltersFactoryImpl(
            context = get()
        )
    }

    factory<GlRenderer> {
        GlRendererImpl(
            context = get(),
            filtersFactory = get()
        )
    }
}
