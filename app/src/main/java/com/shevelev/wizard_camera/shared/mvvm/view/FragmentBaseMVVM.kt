package com.shevelev.wizard_camera.shared.mvvm.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.shevelev.wizard_camera.shared.mvvm.model.InteractorBase
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ShowMessageResCommand
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ShowMessageTextCommand
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ViewCommand
import com.shevelev.wizard_camera.shared.mvvm.view_model.FragmentViewModelFactory
import com.shevelev.wizard_camera.shared.mvvm.view_model.ViewModelBase
import javax.inject.Inject

/**
 * Base class for all fragments
 */
abstract class FragmentBaseMVVM<VDB: ViewDataBinding, VM: ViewModelBase<out InteractorBase>> : FragmentBase<VDB>() {
    private lateinit var _viewModel: VM

    protected val viewModel: VM
        get() = _viewModel

    @Inject
    internal lateinit var viewModelFactory: FragmentViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProviders.of(this, viewModelFactory)[provideViewModelType()]
        _viewModel = viewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _viewModel.command.observe(viewLifecycleOwner) {
            processViewCommandGeneral(it)
        }

        val resultView = super.onCreateView(inflater, container, savedInstanceState)

        linkViewModel(binding, _viewModel)
        return resultView
    }

    abstract fun provideViewModelType(): Class<VM>

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