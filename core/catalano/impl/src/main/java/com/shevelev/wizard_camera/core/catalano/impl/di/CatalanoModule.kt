package com.shevelev.wizard_camera.core.catalano.impl.di

import com.shevelev.wizard_camera.core.catalano.api.facade.ImageProcessor
import com.shevelev.wizard_camera.core.catalano.impl.facade.ImageProcessorImpl
import org.koin.dsl.module

val CatalanoModule = module(createdAtStart = false) {
    factory<ImageProcessor> {
        ImageProcessorImpl()
    }
}