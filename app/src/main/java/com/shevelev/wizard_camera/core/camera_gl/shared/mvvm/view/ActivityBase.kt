package com.shevelev.wizard_camera.core.camera_gl.shared.mvvm.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

abstract class ActivityBase : AppCompatActivity() {
    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()
    }

    override fun onDestroy() {
        if(isFinishing) {
            releaseInjection()
        }
        super.onDestroy()
    }

    protected abstract fun inject()

    protected abstract fun releaseInjection()
}