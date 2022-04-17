package com.shevelev.wizard_camera.core.ui_utils.mvvm.view_model

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shevelev.wizard_camera.core.ui_utils.R
import com.shevelev.wizard_camera.core.ui_utils.mvvm.model.InteractorBase
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view_commands.ShowMessageResCommand
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view_commands.ViewCommand
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

abstract class ViewModelBase<TInteractor: InteractorBase>
constructor(
    protected val interactor: TInteractor
) : ViewModel(), CoroutineScope {
    private val scopeJob: Job = SupervisorJob()

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        handleError(exception)
    }

    /**
     * Context of this scope.
     */
    override val coroutineContext: CoroutineContext = scopeJob + Dispatchers.Main + errorHandler

    /**
     * Direct command for view
     */
    protected val _command = SingleLiveData<ViewCommand>()
    val command: LiveData<ViewCommand>
        get() = _command

    @CallSuper
    override fun onCleared() {
        scopeJob.cancel()
        super.onCleared()
    }

    private fun handleError(error: Throwable){
        Timber.e(error)
        _command.value = ShowMessageResCommand(R.string.generalError)
    }
}