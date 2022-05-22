package com.shevelev.wizard_camera.activity_main.fragment_camera.di

import com.shevelev.wizard_camera.activity_main.fragment_camera.model.CameraFragmentInteractor
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.CameraFragmentInteractorImpl
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.FiltersFacade
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.FiltersFacadeImpl
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.settings.FilterSettingsFacade
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.settings.FilterSettingsFacadeImpl
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.image_capture.ImageCapture
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.image_capture.ImageCaptureImpl
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.orientation.OrientationManager
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.orientation.OrientationManagerImpl
import com.shevelev.wizard_camera.activity_main.fragment_camera.view_model.CameraFragmentViewModel
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl.FilterDisplayDataList
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl.FilterDisplayDataListImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val CameraFragmentModule = module(createdAtStart = false) {
    factory<FilterSettingsFacade> {
        FilterSettingsFacadeImpl(
            filterSettingsRepository = get()
        )
    }

    factory<FilterDisplayDataList> {
        FilterDisplayDataListImpl()
    }

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

    factory<FiltersFacade> {
        FiltersFacadeImpl(
            lastUsedFilterRepository = get(),
            favoriteFilterRepository = get(),
            displayData = get(),
            filterSettings = get()
        )
    }

    factory<CameraFragmentInteractor> {
        CameraFragmentInteractorImpl(
            filters = get(),
            capture = get(),
            orientation = get()
        )
    }

    viewModel {
        CameraFragmentViewModel(
            appContext = get(),
            interactor = get()
        )
    }
}