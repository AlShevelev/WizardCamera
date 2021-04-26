package com.shevelev.wizard_camera.activity_gallery.fragment_editor.di

import androidx.lifecycle.ViewModel
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.EditorFragmentInteractor
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.EditorFragmentInteractorImpl
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.view_model.EditorFragmentViewModel
import com.shevelev.wizard_camera.common_entities.di_scopes.FragmentScope
import com.shevelev.wizard_camera.shared.mvvm.view_model.FragmentViewModelFactory
import com.shevelev.wizard_camera.shared.mvvm.view_model.FragmentViewModelFactoryImpl
import com.shevelev.wizard_camera.shared.mvvm.view_model.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class EditorFragmentModule {
    @Binds
    @FragmentScope
    abstract fun bindViewModelFactory(factory: FragmentViewModelFactoryImpl): FragmentViewModelFactory

    @Binds
    @FragmentScope
    abstract fun provideInteractor(interactor: EditorFragmentInteractorImpl): EditorFragmentInteractor

    @Binds
    @IntoMap
    @ViewModelKey(EditorFragmentViewModel::class)
    abstract fun provideViewModel(model: EditorFragmentViewModel): ViewModel
}