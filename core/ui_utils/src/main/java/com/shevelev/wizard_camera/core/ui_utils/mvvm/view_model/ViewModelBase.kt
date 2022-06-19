package com.shevelev.wizard_camera.core.ui_utils.mvvm.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shevelev.wizard_camera.core.ui_utils.mvvm.model.InteractorBase

abstract class ViewModelBase<TInteractor : InteractorBase, TCommand>(
    protected val interactor: TInteractor
) : ViewModel() {

    /**
     * Direct command for view
     */
    private val _command = SingleLiveData<TCommand>()
    val command: LiveData<TCommand>
        get() = _command

    protected fun sendCommand(command: TCommand) {
        _command.value = command
    }
}