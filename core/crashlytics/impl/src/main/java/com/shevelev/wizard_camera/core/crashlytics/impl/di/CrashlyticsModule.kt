package com.shevelev.wizard_camera.core.crashlytics.impl.di

import com.shevelev.wizard_camera.core.crashlytics.api.CrashlyticsFacade
import com.shevelev.wizard_camera.core.crashlytics.api.device_info.DeviceInfoProvider
import com.shevelev.wizard_camera.core.crashlytics.impl.CrashlyticsFacadeImpl
import com.shevelev.wizard_camera.core.crashlytics.impl.device_info.DeviceInfoProviderImpl
import org.koin.dsl.module

val CrashlyticsModule = module(createdAtStart = false) {
    factory<DeviceInfoProvider> {
        DeviceInfoProviderImpl(
            appContext = get()
        )
    }

    factory<CrashlyticsFacade> {
        CrashlyticsFacadeImpl(
            deviceInfoProvider = get(),
            buildInfo = get()
        )
    }
}