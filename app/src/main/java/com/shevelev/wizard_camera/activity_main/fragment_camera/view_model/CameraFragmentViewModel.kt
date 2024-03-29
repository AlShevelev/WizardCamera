package com.shevelev.wizard_camera.activity_main.fragment_camera.view_model

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.CameraFragmentInteractor
import com.shevelev.wizard_camera.activity_main.fragment_camera.view.gestures.Gesture
import com.shevelev.wizard_camera.activity_main.fragment_camera.view.gestures.Pinch
import com.shevelev.wizard_camera.activity_main.fragment_camera.view.gestures.Tap
import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel.FilterEventsProcessor
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel.FilterListItem
import com.shevelev.wizard_camera.core.ui_kit.lib.flower_menu.FlowerMenuItemData
import com.shevelev.wizard_camera.core.ui_utils.binding_adapters.ButtonState
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view_model.ViewModelBase
import com.shevelev.wizard_camera.core.utils.ext.format
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
internal class CameraFragmentViewModel(
    private val appContext: Context,
    interactor: CameraFragmentInteractor
) : ViewModelBase<CameraFragmentInteractor, CameraFragmentCommand>(interactor),
    FilterEventsProcessor {

    private val _selectedFilter = MutableLiveData(interactor.filters.displayFilter)
    val selectedFilter: LiveData<GlFilterSettings> = _selectedFilter

    private val _screenTitle = MutableLiveData(appContext.getString(interactor.filters.displayFilterTitle))
    val screenTitle: LiveData<String> = _screenTitle

    private val _flashButtonState = MutableLiveData(ButtonState.DISABLED)
    val flashButtonState: LiveData<ButtonState> = _flashButtonState

    private val _isShotButtonEnabled = MutableLiveData(false)
    val isShotButtonEnabled: LiveData<Boolean> = _isShotButtonEnabled

    private val _filtersListData: MutableLiveData<List<FilterListItem>> = MutableLiveData()
    val filtersListData: LiveData<List<FilterListItem>> = _filtersListData

    private val _filtersVisibility = MutableLiveData(View.INVISIBLE)
    val filtersVisibility: LiveData<Int> = _filtersVisibility

    private val _exposureBarVisibility = MutableLiveData(View.INVISIBLE)
    val exposureBarVisibility: LiveData<Int> = _exposureBarVisibility

    private val _flowerFilters = MutableLiveData(interactor.filters.getFiltersForMenu())
    val flowerFilters: LiveData<List<FlowerMenuItemData>> = _flowerFilters

    private val _filtersButtonState = MutableLiveData(ButtonState.ENABLED)
    val filtersButtonState: LiveData<ButtonState> = _filtersButtonState

    private var isSettingsVisible = false

    private var isFlashActive: Boolean = false

    private var exiting = false

    init {
        viewModelScope.launch {
            interactor.filters.init()
        }
    }

    fun processGesture(gesture: Gesture) {
        hideFiltersMenu()

        if (isSettingsVisible) {
            hideSettings()
        } else {
            when (gesture) {
                is Tap -> { /* temporary unused */
                }
                is Pinch -> zoom(gesture.scaleFactor)
            }
        }
    }

    fun onCaptureClick() {
        viewModelScope.launch {
            hideSettings()
            hideFiltersMenu()

            if (interactor.capture.inProgress) {
                sendCommand(CameraFragmentCommand.ShowMessageRes(R.string.capturingInProgressError))
                return@launch
            }

            val filter = interactor.filters.displayFilter
            val screenOrientation = interactor.orientation.screenOrientation
            val startCapturingResult = interactor.capture.startCapture(filter, screenOrientation)

            if (startCapturingResult == null) {
                CameraFragmentCommand.ShowMessageRes(R.string.generalError)
            } else {
                sendCommand(
                    CameraFragmentCommand.StartCapture(
                        startCapturingResult.capturingStream,
                        isFlashActive,
                        interactor.orientation.surfaceRotation
                    )
                )
            }
        }
    }

    fun onCaptureComplete(isSuccess: Boolean) {
        viewModelScope.launch {
            val command = if (isSuccess && interactor.capture.captureCompleted()) {
                CameraFragmentCommand.ShowCapturingSuccess(interactor.orientation.screenOrientation)
            } else {
                CameraFragmentCommand.ShowMessageRes(R.string.generalError)
            }

            sendCommand(command)
        }
    }

    fun onActive() {
        interactor.orientation.start()
    }

    fun onInactive() {
        interactor.orientation.stop()

        sendCommand(CameraFragmentCommand.ResetExposure)

        hideSettings()
    }

    fun onFlashClick() {
        hideSettings()
        hideFiltersMenu()

        isFlashActive = !isFlashActive
        _flashButtonState.value = if (isFlashActive) ButtonState.SELECTED else ButtonState.ENABLED

        val titleRes = if (isFlashActive) R.string.camera_flash_on else R.string.camera_flash_off
        _screenTitle.value = appContext.getString(titleRes)
    }

    @MainThread
    fun onCameraIsSetUp(isFlashSupported: Boolean) {
        _flashButtonState.value = if (isFlashSupported) {
            if (isFlashActive) {
                ButtonState.SELECTED
            } else {
                ButtonState.ENABLED
            }
        } else {
            ButtonState.DISABLED
        }

        _filtersVisibility.value = if (interactor.filters.currentGroup == FiltersGroup.NO_FILTERS) {
            View.INVISIBLE
        } else {
            View.INVISIBLE
        }

        _isShotButtonEnabled.value = true

        _exposureBarVisibility.value = View.VISIBLE
    }

    fun onZoomUpdated(zoomRatio: Float?) {
        zoomRatio?.let { _screenTitle.value = "${appContext.getString(R.string.zoomFactor)} ${it.format("#.00")}" }
    }

    fun onExposeValueUpdated(exposeValue: Float) {
        hideFiltersMenu()
        sendCommand(CameraFragmentCommand.SetExposure(-exposeValue))
    }

    fun onGalleyClick() {
        hideSettings()
        hideFiltersMenu()

        sendCommand(CameraFragmentCommand.NavigateToGallery)
    }

    fun onFiltersMenuClick() {
        hideSettings()

        if (!hideFiltersMenu()) {
            _filtersButtonState.value = ButtonState.SELECTED
            sendCommand(CameraFragmentCommand.SetFlowerMenuVisibility(isVisible = true))
        }
    }

    fun onFilterFromMenuClick(index: Int) {
        viewModelScope.launch {
            hideSettings()
            hideFiltersMenu()

            val group = FiltersGroup.fromIndex(index)!!

            interactor.filters.currentGroup = group

            _selectedFilter.value = interactor.filters.displayFilter
            _screenTitle.value = appContext.getString(interactor.filters.displayFilterTitle)

            if (group == FiltersGroup.NO_FILTERS) {
                _filtersVisibility.value = View.INVISIBLE
            } else {
                _filtersVisibility.value = View.VISIBLE
                _filtersListData.value = interactor.filters.getFiltersListData()
            }
        }
    }

    fun onPermissionDenied() {
        exiting = true
        sendCommand(CameraFragmentCommand.Exit(R.string.needCameraPermissionExit))
    }

    fun onBackClick(): Boolean =
        if (isSettingsVisible) {
            hideSettings()
            false
        } else {
            true
        }

    fun onFilterSettingsChange(settings: GlFilterSettings) {
        viewModelScope.launch {
            hideFiltersMenu()

            interactor.filters.updateSettings(settings)
            _selectedFilter.value = interactor.filters.displayFilter
        }
    }

    override fun onFavoriteFilterClick(code: GlFilterCode, isSelected: Boolean) {
        viewModelScope.launch {
            hideSettings()

            if (isSelected) {
                interactor.filters.addToFavorite(code)
            } else {
                interactor.filters.removeFromFavorite(code)
            }

            _filtersListData.value = interactor.filters.getFiltersListData()
        }
    }

    override fun onSettingsClick(code: GlFilterCode) {
        _exposureBarVisibility.value = View.INVISIBLE
        _filtersVisibility.value = View.INVISIBLE

        sendCommand(CameraFragmentCommand.ShowFilterSettings(interactor.filters.getSettings(code)))
        isSettingsVisible = true
    }

    override fun onFilterClick(code: GlFilterCode, listId: String) {
        viewModelScope.launch {
            hideSettings()

            hideFiltersMenu()

            interactor.filters.selectFilter(code)

            _selectedFilter.value = interactor.filters.displayFilter
            _screenTitle.value = appContext.getString(interactor.filters.displayFilterTitle)

            _filtersListData.value = interactor.filters.getFiltersListData()
        }
    }

    private fun zoom(touchDistance: Float) {
        sendCommand(CameraFragmentCommand.Zoom(touchDistance))
    }

    private fun hideSettings() {
        if (!isSettingsVisible) {
            return
        }

        sendCommand(CameraFragmentCommand.HideFilterSettings)

        _exposureBarVisibility.value = View.VISIBLE
        _filtersVisibility.value = View.VISIBLE

        isSettingsVisible = false
    }

    private fun hideFiltersMenu(): Boolean =
        if (_filtersButtonState.value == ButtonState.SELECTED) {
            _filtersButtonState.value = ButtonState.ENABLED
            sendCommand(CameraFragmentCommand.SetFlowerMenuVisibility(isVisible = false))
            true
        } else {
            false
        }
}