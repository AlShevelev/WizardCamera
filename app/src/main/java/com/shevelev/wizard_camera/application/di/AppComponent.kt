package com.shevelev.wizard_camera.application.di

import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.application.di.scopes.ApplicationScope
import com.shevelev.wizard_camera.main_activity.di.MainActivityComponent
import dagger.Component

@Component(modules = [
    AppModule::class,
    AppModuleChilds::class
])
@ApplicationScope
interface AppComponent {
    val mainActivity: MainActivityComponent.Builder

    fun inject(app: App)
}