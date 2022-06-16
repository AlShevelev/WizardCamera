package com.shevelev.wizard_camera.activity_main.fragment_camera.di

import com.shevelev.wizard_camera.activity_main.fragment_camera.model.CameraFragmentInteractor
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.CameraFragmentInteractorImpl
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.image_capture.ImageCapture
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.image_capture.ImageCaptureImpl
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.orientation.OrientationManager
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.orientation.OrientationManagerImpl
import com.shevelev.wizard_camera.activity_main.fragment_camera.view_model.CameraFragmentViewModel
import com.shevelev.wizard_camera.feature.filters_facade.api.di.FiltersFacadeInjectionSettings
import com.shevelev.wizard_camera.feature.filters_facade.impl.di.FiltersFacadeScope
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.ScopeID
import org.koin.dsl.module


val CameraFragmentModule = module(createdAtStart = false) {
    factory<OrientationManager> {
        OrientationManagerImpl(
            context = get()
        )
    }

    factory<ImageCapture> {
        ImageCaptureImpl(
            photoShotRepository = get(),
            appContext = get()
        )
    }

    factory<CameraFragmentInteractor> { (filterSettings: FiltersFacadeInjectionSettings) ->
        CameraFragmentInteractorImpl(
            filters = FiltersFacadeScope.get(filterSettings.scopeId).get(parameters = { parametersOf(filterSettings) }),
            capture = get(),
            orientation = get()
        )
    }

    viewModel { (filterSettings: FiltersFacadeInjectionSettings) ->
        CameraFragmentViewModel(
            appContext = get(),
            interactor = get(parameters = { parametersOf(filterSettings) })
        )
    }
}