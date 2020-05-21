package com.shevelev.wizard_camera.application.di_storage

import android.app.Application
import com.shevelev.wizard_camera.application.di.AppComponent
import com.shevelev.wizard_camera.application.di.AppModule
import com.shevelev.wizard_camera.application.di.DaggerAppComponent
import com.shevelev.wizard_camera.main_activity.di.MainActivityComponent
import com.shevelev.wizard_camera.utils.id.IdUtil
import kotlin.reflect.KClass

/** Storage for Dagger components on application level  */
class DependencyInjectionStorage(private val app: Application) {

    private val components = mutableMapOf<KClass<*>, MutableMap<String, Any>>()

    inline fun <reified T> get(key: String, vararg args: Any?): T = getComponent(key, T::class, args)

    inline fun <reified T> release(key: String) = releaseComponent(key, T::class)

    @Suppress("UNCHECKED_CAST")
    fun <T> getComponent(key: String, type: KClass<*>, args: Array<out Any?>): T {
        val componentsSet = components[type]

        return if(componentsSet == null) {
            val component = provideComponent<T>(type, args)
            components[type] = mutableMapOf(key to component as Any)

            component
        } else {
            var component = componentsSet[key]
            if(component != null) {
                component as T
            }

            component = provideComponent<T>(type, args)
            componentsSet[key] = component as Any
            component
        }
    }

    fun releaseComponent(key: String, type: KClass<*>) {
        val componentsSet = components[type] ?: return

        componentsSet.remove(key)

        if(componentsSet.isEmpty()) {
            components.remove(type)
        }
    }

    inline fun <reified T> getBase(): T = getBaseComponent(T::class)

    @Suppress("UNCHECKED_CAST")
    fun <T> getBaseComponent(type: KClass<*>): T {
        val componentsSet = components[type]

        return if(componentsSet != null) {
            componentsSet.entries.first().value as T
        } else {
            val component = provideComponent<T>(type, arrayOfNulls(0))
            components[type] = mutableMapOf(IdUtil.generateStringId() to component as Any)

            component
        }
    }

    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    private fun <T> provideComponent(type: KClass<*>, args: Array<out Any?>): T {
        @Suppress("EXPERIMENTAL_API_USAGE")
        return when (type) {
            AppComponent::class -> DaggerAppComponent.builder().appModule(AppModule(app)).build()

            MainActivityComponent::class -> getBase<AppComponent>().mainActivity.build()

            else -> throw UnsupportedOperationException("This component is not supported: ${type.simpleName}")
        } as T
    }
}