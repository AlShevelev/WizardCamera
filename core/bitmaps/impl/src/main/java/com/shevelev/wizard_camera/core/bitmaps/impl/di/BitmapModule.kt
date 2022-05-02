package com.shevelev.wizard_camera.core.bitmaps.impl.di

import com.shevelev.wizard_camera.core.bitmaps.api.type_detector.ImageTypeDetector
import com.shevelev.wizard_camera.core.bitmaps.api.bitmaps.BitmapHelper
import com.shevelev.wizard_camera.core.bitmaps.api.orientation.BitmapOrientationCorrector
import com.shevelev.wizard_camera.core.bitmaps.impl.type_detector.ImageTypeDetectorImpl
import com.shevelev.wizard_camera.core.bitmaps.impl.type_detector.signatures.ImageSignatureFactory
import com.shevelev.wizard_camera.core.bitmaps.impl.bitmaps.BitmapHelperImpl
import com.shevelev.wizard_camera.core.bitmaps.impl.orientation.BitmapOrientationCorrectorImpl
import org.koin.dsl.module

val BitmapsModule = module(createdAtStart = false) {
    factory<ImageTypeDetector> {
        ImageTypeDetectorImpl(
            appContext = get(),
            signatures = listOf(
                ImageSignatureFactory.getJpegSignature(),
                ImageSignatureFactory.getPngSignature()
            )
        )
    }

    factory<BitmapHelper> {
        BitmapHelperImpl(
            appContext = get()
        )
    }

    factory<BitmapOrientationCorrector> {
        BitmapOrientationCorrectorImpl()
    }
}
