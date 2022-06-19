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
    private val filters: FiltersFacade
) : EditorMachineBase(outputCommands, editorStorage) {

    private var isFilterSettingsFacadeSetUp = false

    private var isFilterCarouselSetUp = false

    override suspend fun processEvent(event: InputEvent, state: State): State =
        when {
            state == State.INITIAL && event is InitGlFiltersMachine -> {
                filters.currentGroup = event.group

                editorStorage.isInNoFiltersMode = false

                val lastUsedGlFilter = editorStorage.lastUsedGlFilter ?: filters.getSettings(GlFilterCode.EDGE_DETECTION)
                filters.selectFilter(lastUsedGlFilter.code)
                editorStorage.lastUsedGlFilter = filters.displayFilter

                if (!isFilterCarouselSetUp) {
                    outputCommands.emit(UpdateGlFilterCarousel(filters.getFiltersListData()))
                    isFilterSettingsFacadeSetUp = true
                }

                outputCommands.emit(SetButtonSelection(ModeButtonCode.GL_FILTERS, true))

                outputCommands.emit(
                    SetInitialImage(
                        editorStorage.displayedImage,
                        filters.displayFilter,
                        filters.displayFilterTitle,
                        isMagicMode = false
                    )
                )

                delay(150L)         // To avoid the carousel's flickering
                outputCommands.emit(SetGlFilterCarouselVisibility(true))
                State.MAIN
            }

            state == State.MAIN && event is FiltersMenuButtonClicked -> {
                outputCommands.emit(SetGlFilterCarouselVisibility(false))
                outputCommands.emit(HideGlFilterSettings)
                outputCommands.emit(SetFlowerMenuVisibility(isVisible = true))
                outputCommands.emit(SetButtonSelection(ModeButtonCode.FLOWER_MENU, isSelected = true))
                State.FILTERS_MENU_VISIBLE
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.NO_FILTERS -> {
                editorStorage.onUpdate()
                outputCommands.emit(SetButtonSelection(ModeButtonCode.GL_FILTERS, false))
                outputCommands.emit(SetGlFilterCarouselVisibility(false))
                State.GL_FILTERS_NONE
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

                outputCommands.emit(
                    UpdateImageByGlFilter(
                        settings = filters.displayFilter,
                        filterTitle = filters.displayFilterTitle,
                        filters = filters.getFiltersListData()
                    )
                )

                state
            }

            state == State.MAIN && event is GlFilterFavoriteUpdate -> {
                if (event.isSelected) {
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

                if (editorStorage.isSourceImageDisplayed) {
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
                        )
                    )
                } else {
                    outputCommands.emit(SetButtonSelection(ModeButtonCode.MAGIC, false))
                    editorStorage.switchToSourceImage()
                    outputCommands.emit(
                        SetInitialImage(
                            editorStorage.displayedImage,
                            filter,
                            filters.displayFilterTitle,
                            isMagicMode = true
                        )
                    )
                }
                State.MAIN
            }

            state == State.SETTINGS_VISIBLE && event is ModeButtonClicked && event.code == ModeButtonCode.NO_FILTERS -> {
                editorStorage.onUpdate()
                outputCommands.emit(SetButtonSelection(ModeButtonCode.GL_FILTERS, false))
                hideFilterSettings()
                State.GL_FILTERS_NONE
            }

            state == State.SETTINGS_VISIBLE && event is GlFilterSettingsHid -> {
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

                outputCommands.emit(
                    UpdateImageByGlFilter(
                        settings = event.settings,
                        filterTitle = null,
                        filters = filters.getFiltersListData()
                    )
                )
                state
            }

            state == State.SETTINGS_VISIBLE && event is FiltersMenuButtonClicked -> {
                outputCommands.emit(HideGlFilterSettings)
                outputCommands.emit(SetFlowerMenuVisibility(isVisible = true))
                outputCommands.emit(SetButtonSelection(ModeButtonCode.FLOWER_MENU, isSelected = true))
                State.FILTERS_MENU_VISIBLE
            }

            state == State.FILTERS_MENU_VISIBLE -> {
                outputCommands.emit(SetFlowerMenuVisibility(isVisible = false))
                outputCommands.emit(SetButtonSelection(ModeButtonCode.FLOWER_MENU, isSelected = false))

                val resultState = if (event is FilterFromMenuSelected) {
                    if (event.group == filters.currentGroup) {
                        State.MAIN
                    } else {
                        when (event.group) {
                            FiltersGroup.NO_FILTERS -> State.GL_FILTERS_NONE
                            FiltersGroup.ALL -> State.GL_FILTERS_ALL
                            FiltersGroup.COLORS -> State.GL_FILTERS_COLORS
                            FiltersGroup.DEFORMATIONS -> State.GL_FILTERS_DEFORMATIONS
                            FiltersGroup.STYLIZATION -> State.GL_FILTERS_STYLIZATION
                            FiltersGroup.FAVORITES -> State.GL_FILTERS_FAVORITES
                        }
                    }
                } else {
                    State.MAIN
                }

                if (resultState != State.MAIN) {
                    editorStorage.onUpdate()
                }

                if (resultState != State.GL_FILTERS_NONE) {
                    outputCommands.emit(SetGlFilterCarouselVisibility(true))
                }
                resultState
            }

            else -> state
        }

    private suspend fun hideFilterSettings() {
        outputCommands.emit(HideGlFilterSettings)
        outputCommands.emit(SetGlFilterCarouselVisibility(true))
    }
}