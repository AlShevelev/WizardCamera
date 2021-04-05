package com.shevelev.wizard_camera.shared.mvvm.view

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.shevelev.wizard_camera.utils.id.IdUtil

abstract class FragmentBase: Fragment() {
    protected open val isBackHandlerEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        if(isBackHandlerEnabled) {
            requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(isBackHandlerEnabled) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if(isRemoving) {
            releaseInjection()
        }
    }

    protected open fun inject() {}

    protected open fun releaseInjection() {}

    protected open fun onBackPressed() { }
}