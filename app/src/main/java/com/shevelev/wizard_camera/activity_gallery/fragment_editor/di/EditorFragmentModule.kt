package com.shevelev.wizard_camera.activity_gallery.fragment_editor.di

import androidx.lifecycle.ViewModel
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.EditorFragmentInteractor
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.EditorFragmentInteractorImpl
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.StateMachinesOrchestrator
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.impl.StateMachinesOrchestratorImpl
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorage
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorageImpl
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.view_model.EditorFragmentViewModel
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.settings.FilterSettingsFacade
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.settings.FilterSettingsFacadeImpl
import com.shevelev.wizard_camera.core.common_entities.di_scopes.FragmentScope
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.filters.display_data.gl.FilterDisplayDataList
import com.shevelev.wizard_camera.filters.display_data.gl.FilterDisplayDataListImpl
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view_model.FragmentViewModelFactory
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view_model.FragmentViewModelFactoryImpl
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view_model.ViewModelKey
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
        abstract fun provideEditorStorage(storage: EditorStorageImpl): EditorStorage

        @Binds
        @IntoMap
        @ViewModelKey(EditorFragmentViewModel::class)
        abstract fun provideViewModel(model: EditorFragmentViewModel): ViewModel

        @Binds
        abstract fun provideFilterDisplayDataList(list: FilterDisplayDataListImpl): FilterDisplayDataList

        @Binds
        abstract fun provideFilterSettingsFacade(facade: FilterSettingsFacadeImpl): FilterSettingsFacade
    }
}