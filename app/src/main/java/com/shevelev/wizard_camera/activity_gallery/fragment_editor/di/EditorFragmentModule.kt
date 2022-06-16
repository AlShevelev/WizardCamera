package com.shevelev.wizard_camera.activity_gallery.fragment_editor.di

import com.shevelev.wizard_camera.activity_gallery.di.GalleryActivityScope
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.EditorFragmentInteractor
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.EditorFragmentInteractorImpl
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.StateMachinesOrchestrator
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.impl.StateMachinesOrchestratorImpl
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorage
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorageImpl
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.view_model.EditorFragmentViewModel
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl.FilterDisplayDataList
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl.FilterDisplayDataListImpl
import com.shevelev.wizard_camera.feature.filters_facade.api.di.FiltersFacadeInjectionSettings
import com.shevelev.wizard_camera.feature.filters_facade.impl.di.FiltersFacadeScope
import com.shevelev.wizard_camera.feature.filters_facade.impl.settings.FilterSettingsFacade
import com.shevelev.wizard_camera.feature.filters_facade.impl.settings.FilterSettingsFacadeImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
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

    factory<StateMachinesOrchestrator> { (filterSettings: FiltersFacadeInjectionSettings) ->
        StateMachinesOrchestratorImpl(
            editorStorage = get(parameters = { parametersOf(filterSettings) }),
            filters = FiltersFacadeScope.get(filterSettings.scopeId).get(parameters = { parametersOf(filterSettings) })
        )
    }

    factory<EditorFragmentInteractor> { (filterSettings: FiltersFacadeInjectionSettings) ->
        EditorFragmentInteractorImpl(
            stateMachinesOrchestrator = get(parameters = { parametersOf(filterSettings) }),
            filters = FiltersFacadeScope.get(filterSettings.scopeId).get(parameters = { parametersOf(filterSettings) })
        )
    }

    factory<EditorStorage> { (filterSettings: FiltersFacadeInjectionSettings) ->
        EditorStorageImpl(
            photoShotRepository = get(),
            fragmentsDataPass = GalleryActivityScope.getScope().get(),
            filterSettings = get(),
            bitmapHelper = get(),
            filters = FiltersFacadeScope.get(filterSettings.scopeId).get(parameters = { parametersOf(filterSettings) })
        )
    }

    viewModel { (filterSettings: FiltersFacadeInjectionSettings, sourceShot: PhotoShot) ->
        EditorFragmentViewModel(
            appContext = get(),
            interactor = get(parameters = { parametersOf(filterSettings) }),
            sourceShot = sourceShot
        )
    }
}