package com.shevelev.wizard_camera

import android.app.Application
import com.shevelev.wizard_camera.logging.TimberTreeDebug
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(TimberTreeDebug())
    }
}