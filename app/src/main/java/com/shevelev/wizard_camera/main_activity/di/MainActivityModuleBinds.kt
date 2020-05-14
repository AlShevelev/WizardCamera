package com.shevelev.wizard_camera.main_activity.di

import androidx.lifecycle.ViewModel
import com.shevelev.wizard_camera.application.di.scopes.ActivityScope
import com.shevelev.wizard_camera.main_activity.model.MainActivityModel
import com.shevelev.wizard_camera.main_activity.model.MainActivityModelImpl
import com.shevelev.wizard_camera.main_activity.model.filters_repository.FiltersRepository
import com.shevelev.wizard_camera.main_activity.model.filters_repository.FiltersRepositoryImpl
import com.shevelev.wizard_camera.main_activity.model.image_capture.ImageCapture
import com.shevelev.wizard_camera.main_activity.model.image_capture.ImageCaptureImpl
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
    abstract fun provideFiltersRepository(repository: FiltersRepositoryImpl): FiltersRepository

    @Binds
    abstract fun provideImageCapture(capture: ImageCaptureImpl): ImageCapture
}