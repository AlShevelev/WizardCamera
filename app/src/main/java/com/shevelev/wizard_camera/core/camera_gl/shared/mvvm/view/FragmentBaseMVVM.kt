package com.shevelev.wizard_camera.core.camera_gl.shared.mvvm.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.shevelev.wizard_camera.core.camera_gl.shared.mvvm.model.InteractorBase
import com.shevelev.wizard_camera.core.camera_gl.shared.mvvm.view_commands.ShowMessageResCommand
import com.shevelev.wizard_camera.core.camera_gl.shared.mvvm.view_commands.ShowMessageTextCommand
import com.shevelev.wizard_camera.core.camera_gl.shared.mvvm.view_commands.ViewCommand
import com.shevelev.wizard_camera.core.camera_gl.shared.mvvm.view_model.FragmentViewModelFactory
import com.shevelev.wizard_camera.core.camera_gl.shared.mvvm.view_model.ViewModelBase
import javax.inject.Inject

/**
 * Base class for all fragments
 */
abstract class FragmentBaseMVVM<VDB: ViewDataBinding, VM: ViewModelBase<out InteractorBase>> : FragmentBase<VDB>() {

    protected abstract val viewModel: VM

    @Inject
    internal lateinit var viewModelFactory: FragmentViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel.command.observe(viewLifecycleOwner) {
            processViewCommandGeneral(it)
        }

        val resultView = super.onCreateView(inflater, container, savedInstanceState)

        linkViewModel(binding, viewModel)
        return resultView
    }

    @LayoutRes
    protected abstract fun layoutResId(): Int

    protected abstract fun linkViewModel(binding: VDB, viewModel: VM)

    protected open fun processViewCommand(command: ViewCommand) {}

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): VDB {
        val binding: VDB = DataBindingUtil.inflate(inflater, this.layoutResId(), container, false)
        binding.lifecycleOwner = this

        return binding
    }

    /**
     * Process input _command
     * @return true if the _command has been processed
     */
    private fun processViewCommandGeneral(command: ViewCommand) =
        when(command) {
            is ShowMessageResCommand -> Toast.makeText(context, command.textResId, Toast.LENGTH_LONG).show()
            is ShowMessageTextCommand -> Toast.makeText(context, command.text, Toast.LENGTH_LONG).show()
            else -> processViewCommand(command)
        }
}