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
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val GalleryFragmentModule = module(createdAtStart = false) {
    factory<GalleryFragmentInteractor> {
        GalleryFragmentInteractorImpl(
            photoShotRepository = get(),
            fragmentsDataPass = GalleryActivityScope.getScope().get(),
            imageImporter = get(),
            photoShotDbRepository = get()
        )
    }

    factory<GalleryHelper> {
        GalleryHelperImpl()
    }

    factory<SharingHelper> {
        SharingHelperImpl()
    }

    factory<ImageImporter> {
        ImageImporterImpl(
            bitmapHelper = get(),
            imageTypeDetector = get(),
            photoShotRepository = get()
        )
    }

    viewModel {
        GalleryFragmentViewModel(
            interactor = get(),
            galleryFragmentsEventPass = GalleryActivityScope.getScope().get()
        )
    }
}