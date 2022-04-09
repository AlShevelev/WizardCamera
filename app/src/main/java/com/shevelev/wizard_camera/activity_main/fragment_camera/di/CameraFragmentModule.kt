package com.shevelev.wizard_camera.activity_main.fragment_camera.di

import androidx.lifecycle.ViewModel
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.CameraFragmentInteractor
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.CameraFragmentInteractorImpl
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.FiltersFacade
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.FiltersFacadeImpl
import com.shevelev.wizard_camera.shared.filters_ui.display_data.gl.FilterDisplayDataList
import com.shevelev.wizard_camera.shared.filters_ui.display_data.gl.FilterDisplayDataListImpl
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.settings.FilterSettingsFacade
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.settings.FilterSettingsFacadeImpl
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.image_capture.ImageCapture
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.image_capture.ImageCaptureImpl
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.orientation.OrientationManager
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.orientation.OrientationManagerImpl
import com.shevelev.wizard_camera.activity_main.fragment_camera.view_model.CameraFragmentViewModel
import com.shevelev.wizard_camera.common_entities.di_scopes.FragmentScope
import com.shevelev.wizard_camera.shared.mvvm.view_model.*
import com.shevelev.wizard_camera.shared.mvvm.view_model.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CameraFragmentModule {
    @Binds
    @FragmentScope
    abstract fun bindViewModelFactory(factory: FragmentViewModelFactoryImpl): FragmentViewModelFactory

    @Binds
    @FragmentScope
    abstract fun provideInteractor(model: CameraFragmentInteractorImpl): CameraFragmentInteractor

    @Binds
    @IntoMap
    @ViewModelKey(CameraFragmentViewModel::class)
    abstract fun provideViewModel(model: CameraFragmentViewModel): ViewModel

    @Binds
    abstract fun provideFiltersRepository(repository: FiltersFacadeImpl): FiltersFacade

    @Binds
    abstract fun provideImageCapture(capture: ImageCaptureImpl): ImageCapture

    @Binds
    abstract fun provideOrientationManager(manager: OrientationManagerImpl): OrientationManager

    @Binds
    abstract fun provideFilterDisplayDataList(list: FilterDisplayDataListImpl): FilterDisplayDataList

    @Binds
    abstract fun provideFilterSettingsFacade(facade: FilterSettingsFacadeImpl): FilterSettingsFacade
}