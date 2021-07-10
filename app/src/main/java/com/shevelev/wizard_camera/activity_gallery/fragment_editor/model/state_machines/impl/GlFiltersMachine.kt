package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.impl

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.*
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorage
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.settings.FilterSettingsFacade
import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.EmptyFilterSettings
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.shared.filters_ui.display_data.gl.FilterDisplayDataList
import com.shevelev.wizard_camera.shared.filters_ui.filters_carousel.FilterFavoriteType
import com.shevelev.wizard_camera.shared.filters_ui.filters_carousel.FilterListItem
import com.shevelev.wizard_camera.shared.filters_ui.filters_carousel.FiltersListData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow

class GlFiltersMachine(
    outputCommands: MutableSharedFlow<OutputCommand>,
    dispatchersProvider: DispatchersProvider,
    editorStorage: EditorStorage,
    private val filterDisplayData: FilterDisplayDataList,
    private val filterSettings: FilterSettingsFacade
) : EditorMachineBase(outputCommands, dispatchersProvider, editorStorage) {

    private var isFilterSettingsFacadeSetUp = false

    private var isFilterCarouselSetUp = false

    override suspend fun processEvent(event: InputEvent, state: State): State =
        when {
            state == State.INITIAL && event is Init -> {
                if(!isFilterSettingsFacadeSetUp) {
                    filterSettings.init()
                    isFilterSettingsFacadeSetUp = true
                }

                var lastUsedGlFilter = editorStorage.lastUsedGlFilter
                if(lastUsedGlFilter == null) {
                    lastUsedGlFilter = filterSettings[GlFilterCode.EDGE_DETECTION]
                    editorStorage.lastUsedGlFilter = lastUsedGlFilter
                }

                if(!isFilterCarouselSetUp) {
                    outputCommands.emit(IntiGlFilterCarousel(getFiltersListData()))
                    isFilterSettingsFacadeSetUp = true
                }

                outputCommands.emit(SetButtonSelection(ModeButtonCode.GL_FILTERS, true))
                outputCommands.emit(SetInitialImage(editorStorage.displayedImage,  lastUsedGlFilter))
                delay(150L)         // To avoid the carousel's flickering
                outputCommands.emit(SetGlFilterCarouselVisibility(true))
                State.MAIN
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.NO_FILTERS -> {
                outputCommands.emit(SetButtonSelection(ModeButtonCode.GL_FILTERS, false))
                outputCommands.emit(SetGlFilterCarouselVisibility(false))
                State.NO_FILTERS
            }

            state == State.MAIN && event is CancelClicked -> {
                State.CANCELING
            }

            state == State.MAIN && event is GlFilterSettingsShown -> {
                outputCommands.emit(SetGlFilterCarouselVisibility(false))
                outputCommands.emit(ShowGlFilterSettings(editorStorage.lastUsedGlFilter!!))
                State.SETTINGS_VISIBLE
            }

            state == State.MAIN && event is AcceptClicked -> {
                // todo Update image in a storage (if necessary)
                outputCommands.emit(CloseEditor)
                State.FINAL
            }

            state == State.MAIN && event is GlFilterSwitched -> {
                val filter = editorStorage.getUsedFilter(event.filterId.filterCode) ?: filterSettings[event.filterId.filterCode]

                editorStorage.lastUsedGlFilter = filter
                outputCommands.emit(UpdateImageByGlFilter(filter))

                state
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.MAGIC -> {
                if(editorStorage.isSourceImageDisplayed) {
                    outputCommands.emit(SetButtonSelection(ModeButtonCode.MAGIC, true))
                    outputCommands.emit(SetProgressVisibility(true))
                    editorStorage.switchToHistogramEqualizedImage()
                    outputCommands.emit(SetProgressVisibility(false))
                    outputCommands.emit(SetInitialImage(editorStorage.displayedImage, editorStorage.lastUsedGlFilter!!))
                } else {
                    outputCommands.emit(SetButtonSelection(ModeButtonCode.MAGIC, false))
                    editorStorage.switchToSourceImage()
                    outputCommands.emit(SetInitialImage(editorStorage.displayedImage, editorStorage.lastUsedGlFilter!!))
                }
                State.MAIN
            }

            state == State.SETTINGS_VISIBLE && event is ModeButtonClicked && event.code == ModeButtonCode.NO_FILTERS -> {
                outputCommands.emit(SetButtonSelection(ModeButtonCode.GL_FILTERS, false))
                hideFilterSettings()
                State.NO_FILTERS
            }

            state == State.SETTINGS_VISIBLE && event is GlFilterSettingsHided -> {
                hideFilterSettings()
                State.MAIN
            }

            state == State.SETTINGS_VISIBLE && event is AcceptClicked -> {
                // todo Update image in a storage (if necessary)
                hideFilterSettings()
                outputCommands.emit(CloseEditor)
                State.FINAL
            }

            state == State.SETTINGS_VISIBLE && event is CancelClicked -> {
                hideFilterSettings()
                State.CANCELING
            }

            state == State.SETTINGS_VISIBLE && event is GlFilterSettingsUpdated -> {
                editorStorage.lastUsedGlFilter = event.settings
                outputCommands.emit(UpdateImageByGlFilter(event.settings))
                state
            }

            else -> state
        }

    private fun getFiltersListData() : FiltersListData {
        val startItems = filterDisplayData.map {
            FilterListItem(
                displayData = it,
                favorite = FilterFavoriteType.HIDDEN,
                hasSettings = filterSettings[it.id.filterCode] !is EmptyFilterSettings,
                isSelected = false
            )
        }

        return FiltersListData(filterDisplayData.getIndex(editorStorage.lastUsedGlFilter!!.code), startItems)
    }

    private suspend fun hideFilterSettings() {
        outputCommands.emit(HideGlFilterSettings)
        outputCommands.emit(SetGlFilterCarouselVisibility(true))
    }
}