package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.impl

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.*
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorage
import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.feature.filters_facade.api.FiltersFacade
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow

internal class GlFiltersMachine(
    outputCommands: MutableSharedFlow<OutputCommand>,
    editorStorage: EditorStorage,
    private val filters: FiltersFacade,
    private val group: FiltersGroup
) : EditorMachineBase(outputCommands, editorStorage) {

    private var isFilterSettingsFacadeSetUp = false

    private var isFilterCarouselSetUp = false

    override suspend fun processEvent(event: InputEvent, state: State): State =
        when {
            state == State.INITIAL && event is Init -> {
                filters.currentGroup = group

                editorStorage.isInNoFiltersMode = false

                var lastUsedGlFilter = editorStorage.lastUsedGlFilter
                if(lastUsedGlFilter == null) {
                    lastUsedGlFilter = filters.getSettings(GlFilterCode.EDGE_DETECTION)
                    editorStorage.lastUsedGlFilter = lastUsedGlFilter
                }
                filters.selectFilter(lastUsedGlFilter.code)

                if(!isFilterCarouselSetUp) {
                    outputCommands.emit(UpdateGlFilterCarousel(filters.getFiltersListData()))
                    isFilterSettingsFacadeSetUp = true
                }

                outputCommands.emit(SetButtonSelection(ModeButtonCode.GL_FILTERS, true))

                outputCommands.emit(
                    SetInitialImage(
                        editorStorage.displayedImage,
                        lastUsedGlFilter,
                        filters.displayFilterTitle/*getFilterTitle(lastUsedGlFilter)*/,
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

                filters.selectFilter(event.filterCode)

                editorStorage.lastUsedGlFilter = filters.displayFilter

                outputCommands.emit(UpdateImageByGlFilter(
                    settings = filters.displayFilter,
                    filterTitle = filters.displayFilterTitle,
                    filters = filters.getFiltersListData()
                ))

                state
            }

            state == State.MAIN && event is GlFilterFavoriteUpdate -> {
                if(event.isSelected) {
                    filters.addToFavorite(event.code)
                } else {
                    filters.removeFromFavorite(event.code)
                }

                outputCommands.emit(UpdateGlFilterCarousel(filters.getFiltersListData()))

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
                            filters.displayFilterTitle,
                            isMagicMode = true
                        ))
                } else {
                    outputCommands.emit(SetButtonSelection(ModeButtonCode.MAGIC, false))
                    editorStorage.switchToSourceImage()
                    outputCommands.emit(
                        SetInitialImage(
                            editorStorage.displayedImage,
                            filter,
                            filters.displayFilterTitle,
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

                outputCommands.emit(UpdateImageByGlFilter(
                    settings = event.settings,
                    filterTitle = null,
                    filters = filters.getFiltersListData()
                ))
                state
            }

            else -> state
        }

    private suspend fun hideFilterSettings() {
        outputCommands.emit(HideGlFilterSettings)
        outputCommands.emit(SetGlFilterCarouselVisibility(true))
    }
}