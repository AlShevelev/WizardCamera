package com.shevelev.wizard_camera.main_activity.view_model

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shevelev.wizard_camera.R
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

@SuppressLint("StaticFieldLeak")
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

    private var isFlashActive: Boolean = false

    private var exiting = false

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
                is Tap -> { /* temporary unused */ }
                is Pinch -> zoom(gesture.scaleFactor)
            }
        }
    }

    fun onCaptureClick() {
        launch {
            hideSettings()

            if(model.capture.inProgress) {
                _command.value = ShowMessageResCommand(R.string.capturingInProgressError)
                return@launch
            }

            val filterCode = model.filters.displayFilter.code
            val screenOrientation = model.orientation.screenOrientation
            val targetFile = model.capture.startCapture(filterCode, screenOrientation)

            if(targetFile == null) {
                ShowMessageResCommand(R.string.generalError)
            } else {
                _command.value = StartCaptureCommand(targetFile, isFlashActive)
            }
        }
    }

    fun onCaptureComplete(isSuccess: Boolean) {
        launch {
            _command.value = if(isSuccess) {
                model.capture.captureCompleted()
                ShowCapturingSuccessCommand(model.orientation.screenOrientation)
            } else {
                ShowMessageResCommand(R.string.generalError)
            }
        }
    }

    fun onInactive() {
        _command.value = ResetExposureCommand()

        hideSettings()
    }

    fun onFlashClick() {
        hideSettings()

        isFlashActive = !isFlashActive
        _flashButtonState.value = if(isFlashActive)  ButtonState.SELECTED else ButtonState.ENABLED

        val titleRes = if(isFlashActive) R.string.camera_flash_on else R.string.camera_flash_off
        _screenTitle.value = appContext.getString(titleRes)
    }

    @MainThread
    fun onCameraIsSetUp(isFlashSupported: Boolean) {
        _flashButtonState.value = if(isFlashSupported) {
            if(isFlashActive)  ButtonState.SELECTED else ButtonState.ENABLED
        } else {
            ButtonState.DISABLED
        }

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