package com.shevelev.wizard_camera.activity_gallery.fragment_editor.view_model

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.EditorFragmentInteractor
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.dto.ImageWithFilter
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.*
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel.FilterEventsProcessor
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel.FilterListItem
import com.shevelev.wizard_camera.core.ui_kit.lib.flower_menu.FlowerMenuItemData
import com.shevelev.wizard_camera.core.ui_utils.binding_adapters.ButtonState
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view_model.ViewModelBase
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
internal class EditorFragmentViewModel(
    private val appContext: Context,
    interactor: EditorFragmentInteractor,
    private val sourceShot: PhotoShot
) : ViewModelBase<EditorFragmentInteractor, EditorFragmentCommand>(interactor),
    FilterEventsProcessor {

    private val _screenTitle = MutableLiveData<String?>(null)
    val screenTitle: LiveData<String?> = _screenTitle

    private val _progressVisibility = MutableLiveData(View.GONE)
    val progressVisibility: LiveData<Int> = _progressVisibility

    private val _surfaceVisibility = MutableLiveData(View.GONE)
    val surfaceVisibility: LiveData<Int> = _surfaceVisibility

    private val _glFiltersVisibility = MutableLiveData(View.INVISIBLE)
    val glFiltersVisibility: LiveData<Int> = _glFiltersVisibility

    private val _glSettings = MutableLiveData<GlFilterSettings?>(null)
    val glSettings: LiveData<GlFilterSettings?> = _glSettings

    private val _acceptButtonState = MutableLiveData(ButtonState.ENABLED)
    val acceptButtonState: LiveData<ButtonState> = _acceptButtonState

    private val _cancelButtonState = MutableLiveData(ButtonState.ENABLED)
    val cancelButtonState: LiveData<ButtonState> = _cancelButtonState

    private val _filtersButtonState = MutableLiveData(ButtonState.ENABLED)
    val filtersButtonState: LiveData<ButtonState> = _filtersButtonState

    private val _magicButtonState = MutableLiveData(ButtonState.ENABLED)
    val magicButtonState: LiveData<ButtonState> = _magicButtonState

    private val _imageWithGlFilter = MutableLiveData<ImageWithFilter>(null)
    val imageWithGlFilter: LiveData<ImageWithFilter> = _imageWithGlFilter

    private val _glFilters: MutableLiveData<List<FilterListItem>> = MutableLiveData()
    val glFilters: LiveData<List<FilterListItem>> = _glFilters

    private val _flowerFilters = MutableLiveData(interactor.getFiltersForMenu())
    val flowerFilters: LiveData<List<FlowerMenuItemData>> = _flowerFilters

    init {
        viewModelScope.launch {
            interactor.commands.collect { processOutputCommand(it) }
        }

        viewModelScope.launch {
            _progressVisibility.value = View.VISIBLE
            interactor.init(sourceShot)
            _progressVisibility.value = View.GONE
        }
    }

    override fun onFavoriteFilterClick(code: GlFilterCode, isSelected: Boolean) {
        viewModelScope.launch { interactor.processEvent(GlFilterFavoriteUpdate(code, isSelected)) }
    }

    override fun onSettingsClick(code: GlFilterCode) {
        viewModelScope.launch { interactor.processEvent(GlFilterSettingsShown(code)) }
    }

    override fun onFilterClick(code: GlFilterCode, listId: String) {
        viewModelScope.launch {
            interactor.processEvent(GlFilterSwitched(code))
        }
    }

    fun onMagicButtonClick() {
        viewModelScope.launch { interactor.processEvent(MagicButtonClicked) }
    }

    fun onFiltersMenuButtonClick() {
        viewModelScope.launch {
            interactor.processEvent(FiltersMenuButtonClicked)
        }
    }

    fun onAcceptClick() {
        viewModelScope.launch { interactor.processEvent(AcceptClicked) }
    }

    fun onCancelClick() {
        viewModelScope.launch { interactor.processEvent(CancelClicked) }
    }

    fun onGLFilterSettingsUpdated(setting: GlFilterSettings) {
        viewModelScope.launch { interactor.processEvent(GlFilterSettingsUpdated(setting)) }
    }

    fun onGlSurfaceClick() {
        viewModelScope.launch { interactor.processEvent(GlFilterSettingsHid) }
    }

    fun onFilterFromMenuClick(index: Int) {
        viewModelScope.launch {
            val group  = FiltersGroup.fromIndex(index)!!
            viewModelScope.launch { interactor.processEvent(FilterFromMenuSelected(group)) }
        }
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

            is UpdateGlFilterCarousel -> _glFilters.value = command.items

            is UpdateImageByGlFilter -> {
                _imageWithGlFilter.value?.let { filter ->
                    _imageWithGlFilter.value = filter.copy(settings = command.settings)
                }
                command.filterTitle?.let { _screenTitle.value = appContext.getString(it) }

                _glFilters.value = command.filters
            }

            is ShowGlFilterSettings -> {
                _glSettings.value = command.settings
            }

            is HideGlFilterSettings -> _glSettings.value = null

            is ShowSaveDialog -> sendCommand(EditorFragmentCommand.ShowEditorSaveDialog)

            is CloseEditor -> sendCommand(EditorFragmentCommand.CloseEditor)

            is SetProgressVisibility -> _progressVisibility.value = if(command.isVisible) View.VISIBLE else View.GONE

            is SetFlowerMenuVisibility -> sendCommand(
                EditorFragmentCommand.SetFlowerMenuVisibility(isVisible = command.isVisible)
            )
        }
    }

    private fun setButtonState(button: ModeButtonCode, state: ButtonState) {
        when(button) {
            ModeButtonCode.MAGIC -> _magicButtonState
            ModeButtonCode.FLOWER_MENU -> _filtersButtonState
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