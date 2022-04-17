package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.di

import androidx.lifecycle.ViewModel
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
import com.shevelev.wizard_camera.core.common_entities.di_scopes.FragmentScope
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view_model.FragmentViewModelFactory
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view_model.FragmentViewModelFactoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [GalleryFragmentModule.GalleryFragmentModuleBinds::class])
class GalleryFragmentModule {
    @Module
    abstract class GalleryFragmentModuleBinds {
        @Binds
        @FragmentScope
        abstract fun bindViewModelFactory(factory: FragmentViewModelFactoryImpl): FragmentViewModelFactory

        @Binds
        @FragmentScope
        abstract fun provideInteractor(interactor: GalleryFragmentInteractorImpl): GalleryFragmentInteractor

        @Binds
        @IntoMap
        @com.shevelev.wizard_camera.core.ui_utils.mvvm.view_model.ViewModelKey(GalleryFragmentViewModel::class)
        abstract fun provideViewModel(model: GalleryFragmentViewModel): ViewModel

        @Binds
        abstract fun provideGalleryHelper(helper: GalleryHelperImpl): GalleryHelper

        @Binds
        abstract fun provideSharingHelper(helper: SharingHelperImpl): SharingHelper

        @Binds
        abstract fun provideImageTypeDetector(detector: ImageTypeDetectorImpl): ImageTypeDetector

        @Binds
        abstract fun provideImageImporter(importer: ImageImporterImpl): ImageImporter
    }

    @Provides
    fun provideImageSignaturesList() : List<com.shevelev.wizard_camera.core.bitmaps.impl.type_detector.signatures.ImageSignature> =
        listOf(
            ImageSignatureFactory.getJpegSignature(),
            ImageSignatureFactory.getPngSignature()
        )
}