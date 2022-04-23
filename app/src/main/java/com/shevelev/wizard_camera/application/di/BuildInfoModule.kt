package com.shevelev.wizard_camera.application.di

import com.shevelev.wizard_camera.application.build_info.BuildInfoImpl
import com.shevelev.wizard_camera.core.build_info.api.BuildInfo
import org.koin.dsl.module

val BuildInfoModule = module(createdAtStart = false) {
    single<BuildInfo> {
        BuildInfoImpl(
            appContext = get()
        )
    }
}