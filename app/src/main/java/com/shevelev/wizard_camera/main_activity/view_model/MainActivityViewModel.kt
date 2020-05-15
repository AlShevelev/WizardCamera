package com.shevelev.wizard_camera.main_activity.view_model

import android.view.TextureView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.camera.filter.FilterCode
import com.shevelev.wizard_camera.main_activity.dto.*
import com.shevelev.wizard_camera.main_activity.model.MainActivityModel
import com.shevelev.wizard_camera.main_activity.view.gestures.Gesture
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ShowMessageResCommand
import com.shevelev.wizard_camera.shared.mvvm.view_model.ViewModelBase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel
@Inject
constructor(
    dispatchersProvider: DispatchersProvider,
    model: MainActivityModel
) : ViewModelBase<MainActivityModel>(dispatchersProvider, model) {

    private val _selectedFilter = MutableLiveData(model.filters.selectedFilter)
    val selectedFilter: LiveData<FilterCode> = _selectedFilter

    private val _selectedFilterTitle = MutableLiveData(model.filters.selectedFilterTitle)
    val selectedFilterTitle: LiveData<Int> = _selectedFilterTitle

    private val _isFlashButtonState = MutableLiveData(ButtonState.DISABLED)
    val isFlashButtonState: LiveData<ButtonState> = _isFlashButtonState

    private val _turnFiltersButtonState = MutableLiveData(ButtonState.DISABLED)
    val turnFiltersButtonState: LiveData<ButtonState> = _turnFiltersButtonState

    var isFlashActive: Boolean = false
        private set

    fun processGesture(gesture: Gesture) {
        when(gesture) {
            is Gesture.FlingRight -> model.filters.selectNextFilter()
            is Gesture.FlingLeft -> model.filters.selectPriorFilter()
        }
        _selectedFilter.value = model.filters.selectedFilter
        _selectedFilterTitle.value = model.filters.selectedFilterTitle
    }

    fun onShootClick(textureView: TextureView) {
        launch {
            if(model.capture.inProgress) {
                _command.value = ShowMessageResCommand(R.string.capturingInProgressError)
                return@launch
            }

            val isSuccess = model.capture.capture(textureView, model.filters.selectedFilter)

            _command.value = if(isSuccess) {
                ShowCapturingSuccessCommand()
            } else {
                ShowMessageResCommand(R.string.generalError)
            }
        }
    }

    fun onActive() {
        _isFlashButtonState.value = ButtonState.DISABLED
        _turnFiltersButtonState.value = ButtonState.DISABLED
        _command.value = SetupCameraCommand()
    }

    fun onInactive() {
        _command.value = ReleaseCameraCommand()
    }

    fun onFlashClick() {
        _isFlashButtonState.value = ButtonState.DISABLED
        isFlashActive = !isFlashActive
        _command.value = ReloadCameraCommand()
    }

    fun onCameraIsSetUp() {
        _isFlashButtonState.value = if(isFlashActive)  ButtonState.SELECTED else ButtonState.ACTIVE
        _turnFiltersButtonState.value = if(model.filters.isFilterTurnedOn)  ButtonState.SELECTED else ButtonState.ACTIVE
    }

    fun onTurnFiltersClick() {
        model.filters.switchMode()
        _selectedFilter.value = model.filters.selectedFilter
        _selectedFilterTitle.value = model.filters.selectedFilterTitle
        _turnFiltersButtonState.value = if(model.filters.isFilterTurnedOn)  ButtonState.SELECTED else ButtonState.ACTIVE
    }
}