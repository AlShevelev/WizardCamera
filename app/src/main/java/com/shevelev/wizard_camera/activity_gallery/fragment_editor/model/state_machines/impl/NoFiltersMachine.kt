package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.impl

import androidx.annotation.StringRes
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.*
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorage
import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.feature.filters_facade.api.FiltersFacade
import kotlinx.coroutines.flow.MutableSharedFlow

internal class NoFiltersMachine(
    outputCommands: MutableSharedFlow<OutputCommand>,
    editorStorage: EditorStorage,
    private val filters: FiltersFacade,
    private val group: FiltersGroup
) : EditorMachineBase(outputCommands, editorStorage) {

    override suspend fun processEvent(event: InputEvent, state: State): State =
        when {
            state == State.INITIAL && event is Init -> {
                filters.currentGroup = group

                editorStorage.isInNoFiltersMode = true

                outputCommands.emit(SetButtonSelection(ModeButtonCode.NO_FILTERS, true))
                outputCommands.emit(
                    SetInitialImage(
                        editorStorage.displayedImage,
                        filters.getSettings(GlFilterCode.ORIGINAL),
                        getFilterTitle(),
                        isMagicMode = false
                    )
                )
                State.MAIN
            }

            state == State.MAIN && event is FiltersMenuButtonClicked -> {
                outputCommands.emit(SetFlowerMenuVisibility(true))
                outputCommands.emit(SetButtonSelection(ModeButtonCode.FLOWER_MENU, isSelected = true))
                State.FILTERS_MENU_VISIBLE
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.GL_FILTERS -> {
                editorStorage.onUpdate()
                outputCommands.emit(SetButtonSelection(ModeButtonCode.NO_FILTERS, false))
                State.GL_FILTERS_ALL
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.MAGIC -> {
                editorStorage.onUpdate()

                if (editorStorage.isSourceImageDisplayed) {
                    outputCommands.emit(SetButtonSelection(ModeButtonCode.MAGIC, true))
                    outputCommands.emit(SetProgressVisibility(true))
                    editorStorage.switchToHistogramEqualizedImage()
                    outputCommands.emit(SetProgressVisibility(false))

                    outputCommands.emit(
                        SetInitialImage(
                            editorStorage.displayedImage,
                            filters.getSettings(GlFilterCode.ORIGINAL),
                            getFilterTitle(),
                            isMagicMode = true
                        )
                    )
                } else {
                    outputCommands.emit(SetButtonSelection(ModeButtonCode.MAGIC, false))
                    editorStorage.switchToSourceImage()
                    outputCommands.emit(
                        SetInitialImage(
                            editorStorage.displayedImage,
                            filters.getSettings(GlFilterCode.ORIGINAL),
                            getFilterTitle(),
                            isMagicMode = true
                        )
                    )
                }
                State.MAIN
            }

            state == State.MAIN && event is AcceptClicked -> {
                saveImage()
                outputCommands.emit(CloseEditor)
                State.FINAL
            }

            state == State.MAIN && event is CancelClicked -> {
                State.CANCELING
            }

            state == State.FILTERS_MENU_VISIBLE -> {
                outputCommands.emit(SetFlowerMenuVisibility(false))
                outputCommands.emit(SetButtonSelection(ModeButtonCode.FLOWER_MENU, isSelected = false))

                val resultState = if(event is FilterFromMenuSelected) {
                    when(event.group) {
                        FiltersGroup.NO_FILTERS -> State.MAIN
                        FiltersGroup.ALL -> State.GL_FILTERS_ALL
                        FiltersGroup.COLORS -> State.GL_FILTERS_COLORS
                        FiltersGroup.DEFORMATIONS -> State.GL_FILTERS_DEFORMATIONS
                        FiltersGroup.STYLIZATION -> State.GL_FILTERS_STYLIZATION
                        FiltersGroup.FAVORITES -> State.GL_FILTERS_FAVORITES
                    }
                } else {
                    State.MAIN
                }

                if(resultState != State.MAIN) {
                    editorStorage.onUpdate()
                }
                resultState
            }

            else -> state
        }

    @StringRes
    private fun getFilterTitle() = R.string.filterOriginal
}