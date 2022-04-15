package com.shevelev.wizard_camera.application.di

import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.core.common_entities.di_scopes.ApplicationScope
import com.shevelev.wizard_camera.activity_gallery.di.GalleryActivityComponent
import com.shevelev.wizard_camera.activity_main.di.MainActivityComponent
import dagger.Component

@Component(modules = [
    AppModule::class,
    AppModuleBinds::class,
    AppModuleChild::class
])
@ApplicationScope
interface AppComponent {
    val mainActivity: MainActivityComponent.Builder
    val galleryActivity: GalleryActivityComponent.Builder

    fun inject(app: App)
}