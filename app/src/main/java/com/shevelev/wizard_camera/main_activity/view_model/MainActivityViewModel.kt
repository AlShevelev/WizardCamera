package com.shevelev.wizard_camera.main_activity.view_model

import android.content.Context
import android.graphics.PointF
import android.util.SizeF
import android.view.TextureView
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.camera.camera_settings_repository.CameraSettingsRepository
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
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
    val selectedFilter: LiveData<FilterSettings> = _selectedFilter

    private val _screenTitle = MutableLiveData(appContext.getString(model.filters.displayFilterTitle))
    val screenTitle: LiveData<String> = _screenTitle

    private val _flashButtonState = MutableLiveData(ButtonState.DISABLED)
    val flashButtonState: LiveData<ButtonState> = _flashButtonState

    private val _filterModeButtonState = MutableLiveData(FiltersModeButtonState(FiltersMode.NO_FILTERS, true))
    val filterModeButtonState: LiveData<FiltersModeButtonState> = _filterModeButtonState

    private val _isShotButtonEnabled = MutableLiveData(false)
    val isShotButtonEnabled: LiveData<Boolean> = _isShotButtonEnabled

    private val _autoFocusButtonVisibility = MutableLiveData(View.INVISIBLE)
    val autoFocusButtonVisibility: LiveData<Int> = _autoFocusButtonVisibility

    private val _allFiltersListData: MutableLiveData<FiltersListData> = MutableLiveData()
    val allFiltersListData: LiveData<FiltersListData> = _allFiltersListData

    private val _favoriteFiltersListData: MutableLiveData<FiltersListData> = MutableLiveData()
    val favoriteFiltersListData: LiveData<FiltersListData> = _favoriteFiltersListData

    private val _allFiltersVisibility = MutableLiveData(View.INVISIBLE)
    val allFiltersVisibility: LiveData<Int> = _allFiltersVisibility

    private val _favoritesFiltersVisibility = MutableLiveData(View.INVISIBLE)
    val favoritesFiltersVisibility: LiveData<Int> = _favoritesFiltersVisibility

    private val _exposureBarVisibility = MutableLiveData(View.INVISIBLE)
    val exposureBarVisibility: LiveData<Int> = _exposureBarVisibility

    private var isSettingsVisible = false
    private var isAllFiltersWereVisible = false
    private var isFavoriteFiltersWereVisible = false

    var isFlashActive: Boolean = false
        private set

    var isAutoFocus: Boolean = true
        private set

    var isActive: Boolean = false
        private set

    private var exiting = false

    val cameraSettings: CameraSettingsRepository
        get() = model.cameraSettings

    init {
        launch {
            model.filters.init()
            _allFiltersListData.value = model.filters.getAllFiltersListData()
            model.filters.getFavoriteFiltersListData()?.let { _favoriteFiltersListData.value = it }
        }
    }

    fun processGesture(gesture: Gesture) {
        if(isSettingsVisible) {
            hideSettings()
        } else {
            when (gesture) {
                is Tap -> selectManualFocus(gesture.touchPoint, gesture.touchAreaSize)
                is Pinch -> zoom(gesture.scaleFactor)
            }
        }
    }

    fun onShootClick(textureView: TextureView) {
        launch {
            hideSettings()

            if(model.capture.inProgress) {
                _command.value = ShowMessageResCommand(R.string.capturingInProgressError)
                return@launch
            }

            val screenOrientation = model.orientation.screenOrientation
            val isSuccess = model.capture.capture(textureView, model.filters.displayFilter.code, screenOrientation)

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

//        if(!model.cameraSettings.canUseCamera) {
//            _command.value = ExitCommand(R.string.camera_2_warning)
//        } else {
            _flashButtonState.value = ButtonState.DISABLED
            _filterModeButtonState.value = _filterModeButtonState.value!!.copy(isDisabled = true)
            _isShotButtonEnabled.value = false
            _allFiltersVisibility.value = View.INVISIBLE

            model.orientation.start()

            _command.value = SetupCameraCommand()
//        }
    }

    fun onInactive() {
        isActive = false

        model.orientation.stop()

        isAutoFocus = true
        _autoFocusButtonVisibility.value = View.INVISIBLE
        _command.value = ResetExposureCommand()
        _command.value = ReleaseCameraCommand()

        hideSettings()
    }

    fun onFlashClick() {
        hideSettings()

        isFlashActive = !isFlashActive
        _flashButtonState.value = if(isFlashActive)  ButtonState.SELECTED else ButtonState.ACTIVE
        _command.value = SetFlashStateCommand(isFlashActive)
    }

    fun onCameraIsSetUp() {
        _flashButtonState.value = if(isFlashActive)  ButtonState.SELECTED else ButtonState.ACTIVE

        _filterModeButtonState.value = FiltersModeButtonState(model.filters.filtersMode, false)

        _allFiltersVisibility.value = if(model.filters.filtersMode == FiltersMode.ALL) View.VISIBLE else View.INVISIBLE
        _favoritesFiltersVisibility.value = if(model.filters.filtersMode == FiltersMode.FAVORITE) View.VISIBLE else View.INVISIBLE

        _isShotButtonEnabled.value = true

        _exposureBarVisibility.value = View.VISIBLE
    }

    fun onSwitchFilterModeClick(mode: FiltersMode) {
        launch {
            hideSettings()

            model.filters.filtersMode = mode

            _selectedFilter.value = model.filters.displayFilter
            _screenTitle.value = appContext.getString(model.filters.displayFilterTitle)

            _filterModeButtonState.value = FiltersModeButtonState(model.filters.filtersMode, false)

            if(mode == FiltersMode.FAVORITE) {
                model.filters.getFavoriteFiltersListData()?.let { _favoriteFiltersListData.value = it }
            }

            _allFiltersVisibility.value = if(model.filters.filtersMode == FiltersMode.ALL) View.VISIBLE else View.INVISIBLE
            _favoritesFiltersVisibility.value = if(model.filters.filtersMode == FiltersMode.FAVORITE) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }
    }

    fun onAutoFocusClick() {
        hideSettings()

        isAutoFocus = true
        _autoFocusButtonVisibility.value = View.INVISIBLE
        _screenTitle.value = appContext.getString(R.string.focusAuto)
        _command.value = AutoFocusCommand()
    }

    fun onZoomUpdated(zoomRatio: Float?) {
        zoomRatio?.let { _screenTitle.value = "${appContext.getString(R.string.zoomFactor)} ${it.format("#.00")}" }
    }

    fun onExposeValueUpdated(exposeValue: Float) {
        _command.value = SetExposureCommand(-exposeValue)
    }

    fun onGalleyClick() {
        hideSettings()
        _command.value = NavigateToGalleryCommand()
    }

    fun onPermissionDenied() {
        exiting = true
        _command.value = ExitCommand(R.string.needCameraPermissionExit)
    }

    fun onFilterSelected(filterCode: FilterCode) {
        launch {
            hideSettings()

            model.filters.selectFilter(filterCode)

            if(model.filters.filtersMode == FiltersMode.ALL) {
                _selectedFilter.value = model.filters.displayFilter
                _screenTitle.value = appContext.getString(model.filters.displayFilterTitle)
            }
        }
    }

    fun onFavoriteFilterSelected(filterCode: FilterCode) {
        launch {
            hideSettings()

            model.filters.selectFavoriteFilter(filterCode)

            if(model.filters.filtersMode == FiltersMode.FAVORITE) {
                _selectedFilter.value = model.filters.displayFilter
                _screenTitle.value = appContext.getString(model.filters.displayFilterTitle)
            }
        }
    }

    fun onBackClick(): Boolean =
        if(isSettingsVisible) {
            hideSettings()
            false
        }  else {
            true
        }

    fun onFilterSettingsChange(settings: FilterSettings) {
        launch {
            model.filters.updateSettings(settings)
            _selectedFilter.value = model.filters.displayFilter
        }
    }

    override fun onFavoriteFilterClick(code: FilterCode, isSelected: Boolean) {
        launch {
            hideSettings()

            if(isSelected) {
                model.filters.addToFavorite(code)
            } else {
                model.filters.removeFromFavorite(code)
            }
        }
    }

    override fun onSettingsClick(code: FilterCode) = showSettings(code)

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

    private fun showSettings(code: FilterCode) {
        isAllFiltersWereVisible = _allFiltersVisibility.value == View.VISIBLE
        isFavoriteFiltersWereVisible = _favoritesFiltersVisibility.value == View.VISIBLE

        _exposureBarVisibility.value = View.INVISIBLE
        _allFiltersVisibility.value = View.INVISIBLE
        _favoritesFiltersVisibility.value = View.INVISIBLE

        _command.value = ShowFilterSettingsCommand(model.filters.getSettings(code))
        isSettingsVisible = true
    }

    private fun hideSettings() {
        if(!isSettingsVisible) {
            return
        }

        _command.value = HideFilterSettingsCommand()
        _exposureBarVisibility.value = View.VISIBLE
        if(isAllFiltersWereVisible) {
            _allFiltersVisibility.value = View.VISIBLE
        }
        if(isFavoriteFiltersWereVisible) {
            _favoritesFiltersVisibility.value = View.VISIBLE
        }
        isSettingsVisible = false
    }
}