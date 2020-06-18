package com.shevelev.wizard_camera.gallery_activity.di

import androidx.lifecycle.ViewModel
import com.shevelev.wizard_camera.common_entities.di_scopes.ActivityScope
import com.shevelev.wizard_camera.gallery_activity.model.GalleryActivityModel
import com.shevelev.wizard_camera.gallery_activity.model.GalleryActivityModelImpl
import com.shevelev.wizard_camera.gallery_activity.view_model.GalleryActivityViewModel
import com.shevelev.wizard_camera.shared.mvvm.view_model.ActivityViewModelFactory
import com.shevelev.wizard_camera.shared.mvvm.view_model.ActivityViewModelFactoryImpl
import com.shevelev.wizard_camera.shared.mvvm.view_model.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class GalleryActivityModuleBinds {
    @Binds
    @ActivityScope
    abstract fun bindViewModelFactory(factory: ActivityViewModelFactoryImpl): ActivityViewModelFactory

    @Binds
    @ActivityScope
    abstract fun provideModel(model: GalleryActivityModelImpl): GalleryActivityModel

    @Binds
    @IntoMap
    @ViewModelKey(GalleryActivityViewModel::class)
    abstract fun provideViewModel(model: GalleryActivityViewModel): ViewModel
}