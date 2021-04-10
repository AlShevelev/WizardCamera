package com.shevelev.wizard_camera.application.di

import com.shevelev.wizard_camera.activity_gallery.di.GalleryActivityComponent
import com.shevelev.wizard_camera.activity_main.di.MainActivityComponent
import dagger.Module

@Module(subcomponents = [
    MainActivityComponent::class,
    GalleryActivityComponent::class
])
class AppModuleChild