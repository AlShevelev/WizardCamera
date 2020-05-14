package com.shevelev.wizard_camera.application.di

import com.shevelev.wizard_camera.main_activity.di.MainActivityComponent
import dagger.Module

@Module(subcomponents = [
    MainActivityComponent::class
])
class AppModuleChilds {
}