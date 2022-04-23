package com.shevelev.wizard_camera.application.di

import com.shevelev.wizard_camera.BuildConfig
import com.shevelev.wizard_camera.core.logger.TimberTreeDebug
import com.shevelev.wizard_camera.core.logger.TimberTreeRelease
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.dsl.module
import timber.log.Timber

val AppModule = module(createdAtStart = false) {
    single {
        Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single<Timber.Tree> {
        when(BuildConfig.FLAVOR) {
            "dev" -> TimberTreeDebug()
            "prod" -> TimberTreeRelease(crashlytics = get())
            else -> throw UnsupportedOperationException("This flavor is not supported: ${BuildConfig.FLAVOR}")
        }
    }
}