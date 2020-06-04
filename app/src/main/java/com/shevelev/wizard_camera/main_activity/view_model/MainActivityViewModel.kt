package com.shevelev.wizard_camera.main_activity.view_model

import android.content.Context
import android.graphics.PointF
import android.util.SizeF
import android.view.TextureView
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.main_activity.dto.*
import com.shevelev.wizard_camera.main_activity.model.MainActivityModel
import com.shevelev.wizard_camera.main_activity.view.gestures.*
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ShowMessageResCommand
import com.shevelev.wizard_camera.shared.mvvm.view_model.ViewModelBase
import com.shevelev.wizard_camera.utils.useful_ext.format
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel
@Inject
constructor(
    private val appContext: Context,
    dispatchersProvider: DispatchersProvider,
    model: MainActivityModel
) : ViewModelBase<MainActivityModel>(dispatchersProvider, model),
    FilterEventsProcessor {

    private val _selectedFilter = MutableLiveData(model.filters.displayFilter)
    val selectedFilter: LiveData<FilterCode> = _selectedFilter

    private val _screenTitle = MutableLiveData(appContext.getString(model.filters.displayFilterTitle))
    val screenTitle: LiveData<String> = _screenTitle

    private val _isFlashButtonState = MutableLiveData(ButtonState.DISABLED)
    val isFlashButtonState: LiveData<ButtonState> = _isFlashButtonState

    private val _turnFiltersButtonState = MutableLiveData(ButtonState.DISABLED)
    val turnFiltersButtonState: LiveData<ButtonState> = _turnFiltersButtonState

    private val _autoFocusButtonVisibility = MutableLiveData(View.INVISIBLE)
    val autoFocusButtonVisibility: LiveData<Int> = _autoFocusButtonVisibility

    private val _filtersListData: MutableLiveData<FiltersListData> = MutableLiveData()
    val filtersListData: LiveData<FiltersListData> = _filtersListData

    private val _allFiltersVisibility = MutableLiveData(View.INVISIBLE)
    val allFiltersVisibility: LiveData<Int> = _allFiltersVisibility

    private val _favoritesFiltersVisibility = MutableLiveData(View.INVISIBLE)
    val favoritesFiltersVisibility: LiveData<Int> = _favoritesFiltersVisibility

    var isFlashActive: Boolean = false
        private set

    var isAutoFocus: Boolean = true
        private set

    var isActive: Boolean = false
        private set

    private var exiting = false

    init {
        launch {
            model.filters.init()
            _filtersListData.value = model.filters.getFiltersListData()
        }
    }

    fun processGesture(gesture: Gesture) {
        when(gesture) {
            is Tap -> selectManualFocus(gesture.touchPoint, gesture.touchAreaSize)
            is Pinch -> zoom(gesture.touchDistance)
        }
    }

    fun onShootClick(textureView: TextureView) {
        launch {
            if(model.capture.inProgress) {
                _command.value = ShowMessageResCommand(R.string.capturingInProgressError)
                return@launch
            }

            val screenOrientation = model.orientation.screenOrientation
            val isSuccess = model.capture.capture(textureView, model.filters.displayFilter, screenOrientation)

            _command.value = if(isSuccess) {
                ShowCapturingSuccessCommand(screenOrientation)
            } else {
                ShowMessageResCommand(R.string.generalError)
            }
        }
    }

    fun onActive() {
        if(exiting) {
            return
        }

        isActive = true

        _isFlashButtonState.value = ButtonState.DISABLED
        _turnFiltersButtonState.value = ButtonState.DISABLED
        _allFiltersVisibility.value = View.INVISIBLE

        model.orientation.start()

        _command.value = SetupCameraCommand()
    }

    fun onInactive() {
        isActive = false

        model.orientation.stop()

        isAutoFocus = true
        _autoFocusButtonVisibility.value = View.INVISIBLE
        _command.value = ResetExposureCommand()
        _command.value = ReleaseCameraCommand()
    }

    fun onFlashClick() {
        isFlashActive = !isFlashActive
        _isFlashButtonState.value = if(isFlashActive)  ButtonState.SELECTED else ButtonState.ACTIVE
        _command.value = SetFlashStateCommand(isFlashActive)
    }

    fun onCameraIsSetUp() {
        _isFlashButtonState.value = if(isFlashActive)  ButtonState.SELECTED else ButtonState.ACTIVE
        _turnFiltersButtonState.value = if(model.filters.isFilterTurnedOn)  ButtonState.SELECTED else ButtonState.ACTIVE
        _allFiltersVisibility.value = if(_turnFiltersButtonState.value == ButtonState.SELECTED) View.VISIBLE else View.INVISIBLE
    }

    fun onTurnFiltersClick() {
        model.filters.switchMode()

        _selectedFilter.value = model.filters.displayFilter
        _screenTitle.value = appContext.getString(model.filters.displayFilterTitle)

        _turnFiltersButtonState.value = if(model.filters.isFilterTurnedOn)  ButtonState.SELECTED else ButtonState.ACTIVE
        _allFiltersVisibility.value = if(_turnFiltersButtonState.value == ButtonState.SELECTED) View.VISIBLE else View.INVISIBLE
    }

    fun onAutoFocusClick() {
        isAutoFocus = true
        _autoFocusButtonVisibility.value = View.INVISIBLE
        _screenTitle.value = appContext.getString(R.string.focusAuto)
        _command.value = AutoFocusCommand()
    }

    fun onZoomUpdated(zoomValue: Float) {
        _screenTitle.value = "${appContext.getString(R.string.zoomFactor)} ${zoomValue.format("#.00")}"
    }

    fun onExposeValueUpdated(exposeValue: Float) {
        _command.value = SetExposureCommand(-exposeValue)
    }

    fun onGalleyClick() {
        _command.value = NavigateToGalleryCommand()
    }

    fun onPermissionDenied() {
        exiting = true
        _command.value = ExitCommand(R.string.needCameraPermissionExit)
    }

    fun onFilterSelected(filterCode: FilterCode) {
        launch {
            model.filters.selectFilter(filterCode)

            if(model.filters.isFilterTurnedOn) {
                _selectedFilter.value = model.filters.displayFilter
                _screenTitle.value = appContext.getString(model.filters.displayFilterTitle)
            }
        }
    }

    override fun onFavoriteFilterClick(code: FilterCode, isSelected: Boolean) {
        launch {
            if(isSelected) {
                model.filters.addToFavorite(code)
            } else {
                model.filters.removeFromFavorite(code)
            }
        }
    }

    private fun selectManualFocus(touchPoint: PointF, touchAreaSize: SizeF) {
        _command.value = FocusOnTouchCommand(touchPoint, touchAreaSize)

        if(isAutoFocus) {
            _screenTitle.value = appContext.getString(R.string.focusManual)
        }

        isAutoFocus = false
        _autoFocusButtonVisibility.value = View.VISIBLE
    }

    private fun zoom(touchDistance: Float) {
        _command.value = ZoomCommand(touchDistance)
    }
}