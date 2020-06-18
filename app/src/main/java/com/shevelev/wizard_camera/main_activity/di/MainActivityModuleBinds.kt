package com.shevelev.wizard_camera.main_activity.di

import androidx.lifecycle.ViewModel
import com.shevelev.wizard_camera.common_entities.di_scopes.ActivityScope
import com.shevelev.wizard_camera.main_activity.model.MainActivityModel
import com.shevelev.wizard_camera.main_activity.model.MainActivityModelImpl
import com.shevelev.wizard_camera.main_activity.model.filters_facade.FiltersFacade
import com.shevelev.wizard_camera.main_activity.model.filters_facade.FiltersFacadeImpl
import com.shevelev.wizard_camera.main_activity.model.filters_facade.display_data.FilterDisplayDataList
import com.shevelev.wizard_camera.main_activity.model.filters_facade.display_data.FilterDisplayDataListImpl
import com.shevelev.wizard_camera.main_activity.model.filters_facade.settings.FilterSettingsFacade
import com.shevelev.wizard_camera.main_activity.model.filters_facade.settings.FilterSettingsFacadeImpl
import com.shevelev.wizard_camera.main_activity.model.image_capture.ImageCapture
import com.shevelev.wizard_camera.main_activity.model.image_capture.ImageCaptureImpl
import com.shevelev.wizard_camera.main_activity.model.orientation.OrientationManager
import com.shevelev.wizard_camera.main_activity.model.orientation.OrientationManagerImpl
import com.shevelev.wizard_camera.main_activity.view_model.MainActivityViewModel
import com.shevelev.wizard_camera.shared.mvvm.view_model.ActivityViewModelFactory
import com.shevelev.wizard_camera.shared.mvvm.view_model.ActivityViewModelFactoryImpl
import com.shevelev.wizard_camera.shared.mvvm.view_model.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainActivityModuleBinds {
    @Binds
    @ActivityScope
    abstract fun bindViewModelFactory(factory: ActivityViewModelFactoryImpl): ActivityViewModelFactory

    @Binds
    @ActivityScope
    abstract fun provideModel(model: MainActivityModelImpl): MainActivityModel

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun provideViewModel(model: MainActivityViewModel): ViewModel

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