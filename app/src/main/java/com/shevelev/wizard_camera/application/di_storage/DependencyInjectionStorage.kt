package com.shevelev.wizard_camera.application.di_storage

import android.app.Application
import com.shevelev.wizard_camera.application.di.AppComponent
import com.shevelev.wizard_camera.application.di.AppModule
import com.shevelev.wizard_camera.application.di.DaggerAppComponent
import com.shevelev.wizard_camera.activity_gallery.di.GalleryActivityComponent
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.di.GalleryFragmentComponent
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery_page.di.GalleryPageFragmentComponent
import com.shevelev.wizard_camera.activity_main.di.MainActivityComponent
import com.shevelev.wizard_camera.activity_main.fragment_camera.di.CameraFragmentComponent
import kotlin.reflect.KClass

/** Storage for Dagger components on application level  */
class DependencyInjectionStorage(private val app: Application) {

    private val components = mutableMapOf<KClass<*>, Any>()

    inline fun <reified T> get(vararg args: Any?): T = getComponent(T::class, args)

    inline fun <reified T> release() = releaseComponent(T::class)

    @Suppress("UNCHECKED_CAST")
    fun <T> getComponent(type: KClass<*>, args: Array<out Any?>): T {
        var component = components[type]

        if(component == null) {
            component = provideComponent<T>(type, args)
            components[type] = component as Any
        }

        return component as T
    }

    fun releaseComponent(type: KClass<*>) {
        components.remove(type)
    }

    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    private fun <T> provideComponent(type: KClass<*>, args: Array<out Any?>): T {
        @Suppress("EXPERIMENTAL_API_USAGE")
        return when (type) {
            AppComponent::class -> DaggerAppComponent.builder().appModule(AppModule(app)).build()

            MainActivityComponent::class -> get<AppComponent>().mainActivity.build()

            CameraFragmentComponent::class -> get<MainActivityComponent>().cameraFragment.build()

            GalleryActivityComponent::class -> get<AppComponent>().galleryActivity.build()
            GalleryFragmentComponent::class -> get<GalleryActivityComponent>().galleryFragment.build()
            GalleryPageFragmentComponent::class -> get<GalleryActivityComponent>().galleryPageFragment.build()

            else -> throw UnsupportedOperationException("This component is not supported: ${type.simpleName}")
        } as T
    }
}