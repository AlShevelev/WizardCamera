package com.shevelev.wizard_camera.activity_gallery.fragment_editor.di

import androidx.lifecycle.ViewModel
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.EditorFragmentInteractor
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.EditorFragmentInteractorImpl
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.StateMachinesOrchestrator
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.impl.StateMachinesOrchestratorImpl
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorage
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorageImpl
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.view_model.EditorFragmentViewModel
import com.shevelev.wizard_camera.common_entities.di_scopes.FragmentScope
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.shared.mvvm.view_model.FragmentViewModelFactory
import com.shevelev.wizard_camera.shared.mvvm.view_model.FragmentViewModelFactoryImpl
import com.shevelev.wizard_camera.shared.mvvm.view_model.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [EditorFragmentModule.Bindings::class])
class EditorFragmentModule(private val photoShot: PhotoShot) {
    @Provides
    fun providePhotoShot(): PhotoShot = photoShot

    @Module
    abstract class Bindings {
        @Binds
        @FragmentScope
        abstract fun bindViewModelFactory(factory: FragmentViewModelFactoryImpl): FragmentViewModelFactory

        @Binds
        @FragmentScope
        abstract fun provideInteractor(interactor: EditorFragmentInteractorImpl): EditorFragmentInteractor

        @Binds
        abstract fun provideStateMachinesOrchestrator(orchestrator: StateMachinesOrchestratorImpl): StateMachinesOrchestrator

        @Binds
        @IntoMap
        @ViewModelKey(EditorFragmentViewModel::class)
        abstract fun provideViewModel(model: EditorFragmentViewModel): ViewModel
    }
}