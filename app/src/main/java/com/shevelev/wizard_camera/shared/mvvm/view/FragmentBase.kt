package com.shevelev.wizard_camera.shared.mvvm.view

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import io.golos.utils.id.IdUtil

abstract class FragmentBase: Fragment() {
    companion object {
        private const val INJECTION_KEY = "INJECTION_KEY"
    }

    private lateinit var injectionKey: String

    protected open val isBackHandlerEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injectionKey = savedInstanceState?.getString(INJECTION_KEY) ?: IdUtil.generateStringId()
        inject(injectionKey)

        if(isBackHandlerEnabled) {
            requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(isBackHandlerEnabled) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(INJECTION_KEY, injectionKey)
    }

    override fun onDestroy() {
        super.onDestroy()

        if(isRemoving) {
            releaseInjection(injectionKey)
        }
    }

    protected open fun inject(key: String) {}

    protected open fun releaseInjection(key: String) {}

    protected open fun onBackPressed() { }
}