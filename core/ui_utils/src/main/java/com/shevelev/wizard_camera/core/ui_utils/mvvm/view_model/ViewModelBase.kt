package com.shevelev.wizard_camera.core.ui_utils.mvvm.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shevelev.wizard_camera.core.ui_utils.mvvm.model.InteractorBase
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view_commands.ViewCommand

abstract class ViewModelBase<TInteractor: InteractorBase>
constructor(
    protected val interactor: TInteractor
) : ViewModel() {

    /**
     * Direct command for view
     */
    protected val _command = SingleLiveData<ViewCommand>()
    val command: LiveData<ViewCommand>
        get() = _command
}