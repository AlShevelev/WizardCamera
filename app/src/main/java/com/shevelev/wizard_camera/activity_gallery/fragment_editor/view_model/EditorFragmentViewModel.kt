package com.shevelev.wizard_camera.activity_gallery.fragment_editor.view_model

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.EditorFragmentInteractor
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.dto.ImageWithFilter
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.*
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.ui_utils.binding_adapters.ButtonState
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view_commands.CloseEditorCommand
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view_commands.ShowEditorSaveDialogCommand
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view_model.ViewModelBase
import com.shevelev.wizard_camera.filters.display_data.FilterDisplayId
import com.shevelev.wizard_camera.filters.filters_carousel.FilterEventsProcessor
import com.shevelev.wizard_camera.filters.filters_carousel.FiltersListData
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class EditorFragmentViewModel
@Inject
constructor(
    private val appContext: Context,
    interactor: EditorFragmentInteractor,
    private val sourceShot: PhotoShot,
) : ViewModelBase<EditorFragmentInteractor>(interactor),
    FilterEventsProcessor {

    private val _screenTitle = MutableLiveData<String?>(null)
    val screenTitle: LiveData<String?> = _screenTitle

    private val _progressVisibility = MutableLiveData(View.GONE)
    val progressVisibility: LiveData<Int> = _progressVisibility

    private val _surfaceVisibility = MutableLiveData(View.GONE)
    val surfaceVisibility: LiveData<Int> = _surfaceVisibility

    private val _glFiltersVisibility = MutableLiveData(View.INVISIBLE)
    val glFiltersVisibility: LiveData<Int> = _glFiltersVisibility

    private val _glSettings = MutableLiveData<GlFilterSettings>(null)
    val glSettings: LiveData<GlFilterSettings> = _glSettings

    private val _acceptButtonState = MutableLiveData(ButtonState.ENABLED)
    val acceptButtonState: LiveData<ButtonState> = _acceptButtonState

    private val _cancelButtonState = MutableLiveData(ButtonState.ENABLED)
    val cancelButtonState: LiveData<ButtonState> = _cancelButtonState

    private val _noFiltersButtonState = MutableLiveData(ButtonState.ENABLED)
    val noFiltersButtonState: LiveData<ButtonState> = _noFiltersButtonState

    private val _glFiltersButtonState = MutableLiveData(ButtonState.ENABLED)
    val glFiltersButtonState: LiveData<ButtonState> = _glFiltersButtonState

    private val _magicButtonState = MutableLiveData(ButtonState.ENABLED)
    val magicButtonState: LiveData<ButtonState> = _magicButtonState

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
            interactor.init(sourceShot)
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

                if(!command.isMagicMode) {
                    _screenTitle.value = appContext.getString(command.filterTitle)
                }
            }

            is SetButtonSelection -> if(command.isSelected) {
                setButtonState(command.code, ButtonState.SELECTED)
            } else {
                setButtonState(command.code, ButtonState.ENABLED)
            }

            is SetGlFilterCarouselVisibility -> if(command.isVisible) {
                _glFiltersVisibility.value = View.VISIBLE
            } else {
                _glFiltersVisibility.value = View.INVISIBLE
            }

            is IntiGlFilterCarousel -> _glFilters.value = command.filterData

            is UpdateImageByGlFilter -> {
                _imageWithGlFilter.value?.let { filter ->
                    _imageWithGlFilter.value = filter.copy(settings = command.settings)
                }
                command.filterTitle?.let { _screenTitle.value = appContext.getString(it) }
            }

            is ShowGlFilterSettings -> {
                _glSettings.value = command.settings
            }

            is HideGlFilterSettings -> _glSettings.value = null

            is ShowSaveDialog -> _command.value = ShowEditorSaveDialogCommand

            is CloseEditor -> _command.value = CloseEditorCommand

            is SetProgressVisibility -> _progressVisibility.value = if(command.isVisible) View.VISIBLE else View.GONE
        }
    }

    private fun setButtonState(button: ModeButtonCode, state: ButtonState) {
        when(button) {
            ModeButtonCode.NO_FILTERS -> _noFiltersButtonState
            ModeButtonCode.GL_FILTERS -> _glFiltersButtonState
            ModeButtonCode.MAGIC -> _magicButtonState
        }
            .let { it.value = state }

        if(button == ModeButtonCode.MAGIC) {
            _screenTitle.value = if(state == ButtonState.SELECTED) {
                appContext.getString(R.string.magic_mode_on)
            } else {
                appContext.getString(R.string.magic_mode_off)
            }
        }
    }
}