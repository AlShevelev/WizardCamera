package com.shevelev.wizard_camera.core.camera_gl.shared.mvvm.view_model

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.core.camera_gl.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.core.camera_gl.shared.mvvm.model.InteractorBase
import com.shevelev.wizard_camera.core.camera_gl.shared.mvvm.view_commands.ShowMessageResCommand
import com.shevelev.wizard_camera.core.camera_gl.shared.mvvm.view_commands.ViewCommand
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

abstract class ViewModelBase<TInteractor: InteractorBase>
constructor(
    dispatchersProvider: DispatchersProvider,
    protected val interactor: TInteractor
) : ViewModel(), CoroutineScope {
    private val scopeJob: Job = SupervisorJob()

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        handleError(exception)
    }

    /**
     * Context of this scope.
     */
    override val coroutineContext: CoroutineContext = scopeJob + dispatchersProvider.uiDispatcher + errorHandler

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