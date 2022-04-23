package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.di

import com.shevelev.wizard_camera.activity_gallery.di.GalleryActivityScope
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model.GalleryFragmentInteractor
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model.GalleryFragmentInteractorImpl
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model.image_importer.ImageImporter
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model.image_importer.ImageImporterImpl
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view.external_actions.GalleryHelper
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view.external_actions.GalleryHelperImpl
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view.external_actions.SharingHelper
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view.external_actions.SharingHelperImpl
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view_model.GalleryFragmentViewModel
import com.shevelev.wizard_camera.core.bitmaps.api.type_detector.ImageTypeDetector
import com.shevelev.wizard_camera.core.bitmaps.impl.type_detector.ImageTypeDetectorImpl
import com.shevelev.wizard_camera.core.bitmaps.impl.type_detector.signatures.ImageSignatureFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val GalleryFragmentModule = module(createdAtStart = false) {
    factory<GalleryFragmentInteractor> {
        GalleryFragmentInteractorImpl(
            appContext = get(),
            photoShotRepository = get(),
            filesHelper = get(),
            mediaScanner = get(),
            fragmentsDataPass = GalleryActivityScope.getScope().get(),
            bitmapHelper = get(),
            imageImporter = get()
        )
    }

    factory<GalleryHelper> {
        GalleryHelperImpl()
    }

    factory<SharingHelper> {
        SharingHelperImpl()
    }

    factory<ImageTypeDetector> {
        ImageTypeDetectorImpl(
            appContext = get(),
            signatures = listOf (
                ImageSignatureFactory.getJpegSignature(),
                ImageSignatureFactory.getPngSignature()
            )
        )
    }

    factory<ImageImporter> {
        ImageImporterImpl(
            photoShotRepository = get(),
            filesHelper = get(),
            mediaScanner = get(),
            bitmapHelper = get(),
            imageTypeDetector = get()
        )
    }

    viewModel {
        GalleryFragmentViewModel(
            interactor = get()
        )
    }
}