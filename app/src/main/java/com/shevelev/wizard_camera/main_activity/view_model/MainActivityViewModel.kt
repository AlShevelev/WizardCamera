package com.shevelev.wizard_camera.main_activity.view_model

import android.view.TextureView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shevelev.wizard_camera.camera.filter.FilterCode
import com.shevelev.wizard_camera.main_activity.dto.ReleaseCameraCommand
import com.shevelev.wizard_camera.main_activity.dto.SetupCameraCommand
import com.shevelev.wizard_camera.main_activity.model.MainActivityModel
import com.shevelev.wizard_camera.main_activity.view.gestures.Gesture
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
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

    fun processGesture(gesture: Gesture) {
        when(gesture) {
            is Gesture.FlingRight -> model.filters.selectNext()
            is Gesture.FlingLeft -> model.filters.selectPrior()
        }
        _selectedFilter.value = model.filters.selectedFilter
        _selectedFilterTitle.value = model.filters.selectedFilterTitle
    }

    fun onShootClick(textureView: TextureView) {
        launch {
            if(model.capture.inProgress) {
                // Show error
                return@launch
            }

            val isSuccess = model.capture.capture(textureView, model.filters.selectedFilter)

            if(!isSuccess) {
                // show error
            }
        }
    }

    fun onActive() {
        _command.value = SetupCameraCommand()
    }

    fun onInactive() {
        _command.value = ReleaseCameraCommand()
    }
}