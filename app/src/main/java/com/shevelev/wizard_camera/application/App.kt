package com.shevelev.wizard_camera.application

import android.annotation.SuppressLint
import android.app.Application
import com.shevelev.wizard_camera.application.di.AppComponent
import com.shevelev.wizard_camera.application.di_storage.DependencyInjectionStorage
import com.shevelev.wizard_camera.utils.id.IdUtil
import timber.log.Timber
import javax.inject.Inject

class App : Application() {
    @Inject
    internal lateinit var timberTree: Timber.Tree

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var injections : DependencyInjectionStorage
            private set
    }

    override fun onCreate() {
        super.onCreate()

        injections = DependencyInjectionStorage(this)
        injections.get<AppComponent>(IdUtil.generateStringId()).inject(this)

        Timber.plant(timberTree)
    }
}