package com.shevelev.wizard_camera.activity_gallery.fragment_editor.di

import com.shevelev.wizard_camera.activity_gallery.di.GalleryActivityScope
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.EditorFragmentInteractor
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.EditorFragmentInteractorImpl
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.StateMachinesOrchestrator
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.impl.StateMachinesOrchestratorImpl
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorage
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorageImpl
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.view_model.EditorFragmentViewModel
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.settings.FilterSettingsFacade
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.settings.FilterSettingsFacadeImpl
import com.shevelev.wizard_camera.filters.display_data.gl.FilterDisplayDataList
import com.shevelev.wizard_camera.filters.display_data.gl.FilterDisplayDataListImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val EditorFragmentModule = module(createdAtStart = false) {
    factory<FilterSettingsFacade> {
        FilterSettingsFacadeImpl(
            filterSettingsRepository = get()
        )
    }

    factory<FilterDisplayDataList> {
        FilterDisplayDataListImpl()
    }

    factory<StateMachinesOrchestrator> {
        StateMachinesOrchestratorImpl(
            editorStorage = get(),
            filterDisplayData = get(),
            filterSettings = get()
        )
    }

    factory<EditorFragmentInteractor> {
        EditorFragmentInteractorImpl(
            stateMachinesOrchestrator = get()
        )
    }

    factory<EditorStorage> {
        EditorStorageImpl(
            photoShotRepository = get(),
            fragmentsDataPass = GalleryActivityScope.getScope().get(),
            filterSettings = get(),
            bitmapHelper = get()
        )
    }

    viewModel { params ->
        EditorFragmentViewModel(
            appContext = get(),
            interactor = get(),
            sourceShot = params.get()
        )
    }
}