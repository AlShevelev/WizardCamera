package com.shevelev.wizard_camera.shared.mvvm.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.shevelev.wizard_camera.databinding.FragmentGalleryPageBinding
import com.shevelev.wizard_camera.shared.glide.clear
import com.shevelev.wizard_camera.utils.id.IdUtil
import com.shevelev.wizard_camera.utils.useful_ext.ifNotNull

abstract class FragmentBase<VB: ViewBinding>: Fragment() {
    protected open val isBackHandlerEnabled = false

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding!!

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = createBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
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

    protected abstract fun createBinding(inflater: LayoutInflater, container: ViewGroup?): VB
}