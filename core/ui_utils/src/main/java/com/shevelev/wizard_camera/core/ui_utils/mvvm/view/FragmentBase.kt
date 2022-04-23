package com.shevelev.wizard_camera.core.ui_utils.mvvm.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class FragmentBase<VB: ViewBinding>: Fragment() {
    protected open val isBackHandlerEnabled = false

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding!!

    private var isDestroyedBySystem = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(isBackHandlerEnabled) {
            requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(isBackHandlerEnabled) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = createBinding(inflater, container)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        isDestroyedBySystem = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        isDestroyedBySystem = true
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    open fun onDialogResult(isCanceled: Boolean, requestCode: Int, data: Any?) {}

    protected open fun onBackPressed() { }

    protected abstract fun createBinding(inflater: LayoutInflater, container: ViewGroup?): VB
}