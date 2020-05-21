package com.shevelev.wizard_camera.shared.mvvm.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.shevelev.wizard_camera.utils.id.IdUtil

abstract class ActivityBase : AppCompatActivity() {
    companion object {
        private const val INJECTION_KEY = "INJECTION_KEY"
    }

    private lateinit var injectionKey: String

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injectionKey = savedInstanceState?.getString(INJECTION_KEY) ?: IdUtil.generateStringId()
        inject(injectionKey)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(INJECTION_KEY, injectionKey)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        if(isFinishing) {
            releaseInjection(injectionKey)
        }
        super.onDestroy()
    }

    protected abstract fun inject(key: String)

    protected abstract fun releaseInjection(key: String)
}