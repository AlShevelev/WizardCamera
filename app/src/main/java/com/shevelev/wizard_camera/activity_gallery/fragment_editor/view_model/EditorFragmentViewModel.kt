package com.shevelev.wizard_camera.activity_gallery.fragment_editor.view_model

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.EditorFragmentInteractor
import com.shevelev.wizard_camera.shared.binding_adapters.ButtonState
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.shared.mvvm.view_model.ViewModelBase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditorFragmentViewModel
@Inject
constructor(
    dispatchersProvider: DispatchersProvider,
    interactor: EditorFragmentInteractor
) : ViewModelBase<EditorFragmentInteractor>(dispatchersProvider, interactor) {

    private val _progressVisibility = MutableLiveData(View.GONE)
    val progressVisibility: LiveData<Int> = _progressVisibility

    private val _croppingVisibility = MutableLiveData(View.GONE)
    val croppingVisibility: LiveData<Int> = _croppingVisibility

    private val _surfaceVisibility = MutableLiveData(View.GONE)
    val surfaceVisibility: LiveData<Int> = _surfaceVisibility

    private val _glFiltersVisibility = MutableLiveData(View.GONE)
    val glFiltersVisibility: LiveData<Int> = _glFiltersVisibility

    private val _glSettingsVisibility = MutableLiveData(View.GONE)
    val glSettingsVisibility: LiveData<Int> = _glSettingsVisibility

    private val _systemFiltersVisibility = MutableLiveData(View.GONE)
    val systemFiltersVisibility: LiveData<Int> = _systemFiltersVisibility

    private val _acceptButtonState = MutableLiveData(ButtonState.ENABLED)
    val acceptButtonState: LiveData<ButtonState> = _acceptButtonState

    private val _cancelButtonState = MutableLiveData(ButtonState.ENABLED)
    val cancelButtonState: LiveData<ButtonState> = _cancelButtonState

    private val _noFiltersButtonState = MutableLiveData(ButtonState.ENABLED)
    val noFiltersButtonState: LiveData<ButtonState> = _noFiltersButtonState

    private val _glFiltersButtonState = MutableLiveData(ButtonState.ENABLED)
    val glFiltersButtonState: LiveData<ButtonState> = _glFiltersButtonState

    private val _systemFiltersButtonState = MutableLiveData(ButtonState.ENABLED)
    val systemFiltersButtonState: LiveData<ButtonState> = _systemFiltersButtonState

    private val _cropButtonState = MutableLiveData(ButtonState.ENABLED)
    val cropButtonState: LiveData<ButtonState> = _cropButtonState

    init {
        launch {
            _progressVisibility.value = View.VISIBLE
            interactor.init()
            _progressVisibility.value = View.GONE
        }
    }
}