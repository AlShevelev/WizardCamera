package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.impl

import androidx.annotation.StringRes
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.*
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorage
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.settings.FilterSettingsFacade
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.EmptyFilterSettings
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl.FilterDisplayDataList
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel.FilterFavoriteType
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel.FilterListItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow

class GlFiltersMachine(
    outputCommands: MutableSharedFlow<OutputCommand>,
    editorStorage: EditorStorage,
    private val filterDisplayData: FilterDisplayDataList,
    private val filterSettings: FilterSettingsFacade
) : EditorMachineBase(outputCommands, editorStorage) {

    private var isFilterSettingsFacadeSetUp = false

    private var isFilterCarouselSetUp = false

    override suspend fun processEvent(event: InputEvent, state: State): State =
        when {
            state == State.INITIAL && event is Init -> {
                editorStorage.isInNoFiltersMode = false

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

                outputCommands.emit(
                    SetInitialImage(
                        editorStorage.displayedImage,
                        lastUsedGlFilter,
                        getFilterTitle(lastUsedGlFilter),
                        isMagicMode = false
                    ))

                delay(150L)         // To avoid the carousel's flickering
                outputCommands.emit(SetGlFilterCarouselVisibility(true))
                State.MAIN
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.NO_FILTERS -> {
                editorStorage.onUpdate()
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
                saveImage()
                outputCommands.emit(CloseEditor)
                State.FINAL
            }

            state == State.MAIN && event is GlFilterSwitched -> {
                editorStorage.onUpdate()
                val filter = editorStorage.getUsedFilter(event.filterCode) ?: filterSettings[event.filterCode]

                editorStorage.lastUsedGlFilter = filter
                outputCommands.emit(UpdateImageByGlFilter(filter, getFilterTitle(filter)))

                state
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.MAGIC -> {
                editorStorage.onUpdate()
                val filter = editorStorage.lastUsedGlFilter!!

                if(editorStorage.isSourceImageDisplayed) {
                    outputCommands.emit(SetButtonSelection(ModeButtonCode.MAGIC, true))
                    outputCommands.emit(SetProgressVisibility(true))
                    editorStorage.switchToHistogramEqualizedImage()
                    outputCommands.emit(SetProgressVisibility(false))

                    outputCommands.emit(
                        SetInitialImage(
                            editorStorage.displayedImage,
                            filter,
                            getFilterTitle(filter),
                            isMagicMode = true
                        ))
                } else {
                    outputCommands.emit(SetButtonSelection(ModeButtonCode.MAGIC, false))
                    editorStorage.switchToSourceImage()
                    outputCommands.emit(
                        SetInitialImage(
                            editorStorage.displayedImage,
                            filter,
                            getFilterTitle(filter),
                            isMagicMode = true
                        ))
                }
                State.MAIN
            }

            state == State.SETTINGS_VISIBLE && event is ModeButtonClicked && event.code == ModeButtonCode.NO_FILTERS -> {
                editorStorage.onUpdate()
                outputCommands.emit(SetButtonSelection(ModeButtonCode.GL_FILTERS, false))
                hideFilterSettings()
                State.NO_FILTERS
            }

            state == State.SETTINGS_VISIBLE && event is GlFilterSettingsHided -> {
                hideFilterSettings()
                State.MAIN
            }

            state == State.SETTINGS_VISIBLE && event is AcceptClicked -> {
                hideFilterSettings()
                saveImage()
                outputCommands.emit(CloseEditor)
                State.FINAL
            }

            state == State.SETTINGS_VISIBLE && event is CancelClicked -> {
                hideFilterSettings()
                State.CANCELING
            }

            state == State.SETTINGS_VISIBLE && event is GlFilterSettingsUpdated -> {
                editorStorage.onUpdate()
                editorStorage.lastUsedGlFilter = event.settings
                outputCommands.emit(UpdateImageByGlFilter(event.settings, null))
                state
            }

            else -> state
        }

    private fun getFiltersListData() : List<FilterListItem> {
        val selectedFilterCode = editorStorage.lastUsedGlFilter!!.code

        return filterDisplayData.map {
            FilterListItem(
                listId = "",        // There is only one list in the editor screen, so never mind about it
                displayData = it,
                favorite = FilterFavoriteType.HIDDEN,
                hasSettings = filterSettings[it.code] !is EmptyFilterSettings,
                isSelected = it.code == selectedFilterCode
            )
        }
    }

    private suspend fun hideFilterSettings() {
        outputCommands.emit(HideGlFilterSettings)
        outputCommands.emit(SetGlFilterCarouselVisibility(true))
    }

    @StringRes
    private fun getFilterTitle(filter: GlFilterSettings): Int = filterDisplayData[filter.code].title
}