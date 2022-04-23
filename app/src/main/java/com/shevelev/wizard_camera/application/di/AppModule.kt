package com.shevelev.wizard_camera.application.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.dsl.module

val AppModule = module(createdAtStart = false) {
    single {
        Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}