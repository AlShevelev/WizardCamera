package com.shevelev.wizard_camera.activity_gallery.fragment_editor.view_model

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.EditorFragmentInteractor
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.dto.ImageWithFilter
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.*
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.system.SystemFilterSettings
import com.shevelev.wizard_camera.shared.filters_ui.filters_carousel.FilterEventsProcessor
import com.shevelev.wizard_camera.shared.binding_adapters.ButtonState
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.shared.filters_ui.display_data.FilterDisplayId
import com.shevelev.wizard_camera.shared.filters_ui.filters_carousel.FiltersListData
import com.shevelev.wizard_camera.shared.mvvm.view_model.ViewModelBase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditorFragmentViewModel
@Inject
constructor(
    dispatchersProvider: DispatchersProvider,
    interactor: EditorFragmentInteractor
) : ViewModelBase<EditorFragmentInteractor>(dispatchersProvider, interactor),
    FilterEventsProcessor {

    private val _screenTitle = MutableLiveData("")
    val screenTitle: LiveData<String> = _screenTitle

    private val _progressVisibility = MutableLiveData(View.GONE)
    val progressVisibility: LiveData<Int> = _progressVisibility

    private val _croppingVisibility = MutableLiveData(View.GONE)
    val croppingVisibility: LiveData<Int> = _croppingVisibility

    private val _surfaceVisibility = MutableLiveData(View.GONE)
    val surfaceVisibility: LiveData<Int> = _surfaceVisibility

    private val _glFiltersVisibility = MutableLiveData(View.INVISIBLE)
    val glFiltersVisibility: LiveData<Int> = _glFiltersVisibility

    private val _glSettings = MutableLiveData<GlFilterSettings>(null)
    val glSettings: LiveData<GlFilterSettings> = _glSettings

    private val _systemFiltersVisibility = MutableLiveData(View.INVISIBLE)
    val systemFiltersVisibility: LiveData<Int> = _systemFiltersVisibility

    private val _systemSettings = MutableLiveData<SystemFilterSettings>(null)
    val systemSettings: LiveData<SystemFilterSettings> = _systemSettings

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

    private val _imageWithGlFilter = MutableLiveData<ImageWithFilter>(null)
    val imageWithGlFilter: LiveData<ImageWithFilter> = _imageWithGlFilter

    private val _glFilters: MutableLiveData<FiltersListData> = MutableLiveData()
    val glFilters: LiveData<FiltersListData> = _glFilters

    init {
        launch {
            interactor.commands.collect { processOutputCommand(it) }
        }

        launch {
            _progressVisibility.value = View.VISIBLE
            interactor.init()
            _progressVisibility.value = View.GONE
        }
    }

    override fun onSettingsClick(id: FilterDisplayId) {
        launch { interactor.processEvent(GlFilterSettingsShown) }
    }

    fun onModeButtonClick(code: ModeButtonCode) {
        launch { interactor.processEvent(ModeButtonClicked(code)) }
    }

    fun onAcceptClick() {
        launch { interactor.processEvent(AcceptClicked) }
    }

    fun onCancelClick() {
        launch { interactor.processEvent(CancelClicked) }
    }

    fun onGLFilterSelected(filterId: FilterDisplayId) {
        launch {
            interactor.processEvent(GlFilterSwitched(filterId))
        }
    }

    fun onGLFilterSettingsUpdated(setting: GlFilterSettings) {
        launch { interactor.processEvent(GlFilterSettingsUpdated(setting)) }
    }

    fun onGlSurfaceClick() {
        launch { interactor.processEvent(GlFilterSettingsHided) }
    }

    private fun processOutputCommand(command: OutputCommand) {
        when(command) {
            is SetInitialImage -> {
                _imageWithGlFilter.value = ImageWithFilter(command.image, command.settings)
                _surfaceVisibility.value = View.VISIBLE
            }

            is SelectButton -> setButtonState(command.code, ButtonState.SELECTED)
            is UnSelectButton -> setButtonState(command.code, ButtonState.ENABLED)

            is UpdateSystemFilter -> { }

            is ShowGlFilterCarousel -> _glFiltersVisibility.value = View.VISIBLE
            is IntiGlFilterCarousel -> _glFilters.value = command.filterData
            is UpdateImageByGlFilter -> {
                _imageWithGlFilter.value?.let { filter ->
                    _imageWithGlFilter.value = filter.copy(settings = command.settings)
                }
            }
            is HideGlFilterCarousel -> _glFiltersVisibility.value = View.INVISIBLE

            is ShowGlFilterSettings -> {
                _glSettings.value = command.settings
            }

            is HideGlFilterSettings -> _glSettings.value = null

            is ShowSystemFilterCarousel -> _systemFiltersVisibility.value = View.VISIBLE

            is ScrollSystemFilterCarousel -> { }

            is HideSystemFilterCarousel -> {
                _systemFiltersVisibility.value = View.INVISIBLE
                _systemSettings.value = null
            }

            is ShowCroppingImage -> _croppingVisibility.value = View.VISIBLE
            is HideCroppingImage -> _croppingVisibility.value = View.GONE

            is ShowSaveDialog -> { }

            is CloseEditor -> { }
        }
    }

    private fun setButtonState(button: ModeButtonCode, state: ButtonState) {
        when(button) {
            ModeButtonCode.NO_FILTERS -> _noFiltersButtonState
            ModeButtonCode.GL_FILTERS -> _glFiltersButtonState
            ModeButtonCode.SYSTEM_FILTERS -> _systemFiltersButtonState
            ModeButtonCode.CROP -> _cropButtonState
        }
            .let { it.value = state }
    }
}