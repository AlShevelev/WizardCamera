package com.shevelev.wizard_camera.shared.mvvm.view

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.shevelev.wizard_camera.shared.mvvm.model.ModelBase
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ShowMessageResCommand
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ViewCommand
import com.shevelev.wizard_camera.shared.mvvm.view_model.ActivityViewModelFactory
import com.shevelev.wizard_camera.shared.mvvm.view_model.ViewModelBase
import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber
import javax.inject.Inject

/**
 * Base class for all activities
 */
abstract class ActivityBaseMVVM<VDB : ViewDataBinding, VM : ViewModelBase<out ModelBase>> : ActivityBase() {
    protected lateinit var binding: VDB

    private lateinit var _viewModel: VM
    protected val viewModel: VM
        get() = _viewModel

    @Inject
    internal lateinit var viewModelFactory: ActivityViewModelFactory

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, this.layoutResId())
        binding.lifecycleOwner = this

        val viewModel = ViewModelProviders.of(this, viewModelFactory)[provideViewModelType()]
        _viewModel = viewModel
        linkViewModel(binding, _viewModel)

        _viewModel.command.observe(this, { processViewCommandGeneral(it) })
    }

    abstract fun provideViewModelType(): Class<VM>

    @LayoutRes
    protected abstract fun layoutResId(): Int

    protected abstract fun linkViewModel(binding: VDB, viewModel: VM)

    protected open fun processViewCommand(command: ViewCommand) {}

    /**
     * Process input _command
     * @return true if the _command has been processed
     */
    private fun processViewCommandGeneral(command: ViewCommand) =
        when (command) {
            is ShowMessageResCommand -> Toast.makeText(this, command.textResId, Toast.LENGTH_LONG).show()
            else -> processViewCommand(command)
        }
}