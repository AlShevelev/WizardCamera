package com.shevelev.wizard_camera.core.ui_utils.mvvm.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.shevelev.wizard_camera.core.ui_utils.mvvm.model.InteractorBase
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view_model.ViewModelBase

/**
 * Base class for all fragments
 */
abstract class FragmentBaseMVVM<VDB : ViewDataBinding, VM : ViewModelBase<out InteractorBase, VCommand>, VCommand> :
    FragmentBase<VDB>() {

    protected abstract val viewModel: VM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel.command.observe(viewLifecycleOwner) {
            processViewCommand(it)
        }

        val resultView = super.onCreateView(inflater, container, savedInstanceState)

        linkViewModel(binding, viewModel)
        return resultView
    }

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): VDB {
        val binding: VDB = DataBindingUtil.inflate(inflater, this.layoutResId(), container, false)
        binding.lifecycleOwner = this

        return binding
    }

    @LayoutRes
    protected abstract fun layoutResId(): Int

    protected abstract fun linkViewModel(binding: VDB, viewModel: VM)

    protected open fun processViewCommand(command: VCommand) {}

    protected fun showMessage(@StringRes textResId: Int) {
        Toast.makeText(context, textResId, Toast.LENGTH_LONG).show()
    }

    protected fun showMessage(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}