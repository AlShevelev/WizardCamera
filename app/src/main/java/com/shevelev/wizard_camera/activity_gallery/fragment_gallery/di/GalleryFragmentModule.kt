package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.di

import androidx.lifecycle.ViewModel
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model.GalleryFragmentInteractor
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model.GalleryFragmentInteractorImpl
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view_model.GalleryFragmentViewModel
import com.shevelev.wizard_camera.common_entities.di_scopes.FragmentScope
import com.shevelev.wizard_camera.shared.mvvm.view_model.FragmentViewModelFactory
import com.shevelev.wizard_camera.shared.mvvm.view_model.FragmentViewModelFactoryImpl
import com.shevelev.wizard_camera.shared.mvvm.view_model.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class GalleryFragmentModule {
    @Binds
    @FragmentScope
    abstract fun bindViewModelFactory(factory: FragmentViewModelFactoryImpl): FragmentViewModelFactory

    @Binds
    @FragmentScope
    abstract fun provideInteractor(interactor: GalleryFragmentInteractorImpl): GalleryFragmentInteractor

    @Binds
    @IntoMap
    @ViewModelKey(GalleryFragmentViewModel::class)
    abstract fun provideViewModel(model: GalleryFragmentViewModel): ViewModel
}