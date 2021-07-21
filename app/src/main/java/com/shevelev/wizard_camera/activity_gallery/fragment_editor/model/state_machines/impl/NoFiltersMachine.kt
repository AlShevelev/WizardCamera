package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.impl

import androidx.annotation.StringRes
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.*
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorage
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.settings.FilterSettingsFacade
import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import kotlinx.coroutines.flow.MutableSharedFlow

class NoFiltersMachine(
    outputCommands: MutableSharedFlow<OutputCommand>,
    dispatchersProvider: DispatchersProvider,
    editorStorage: EditorStorage,
    private val filterSettings: FilterSettingsFacade
) : EditorMachineBase(outputCommands, dispatchersProvider, editorStorage) {

    override suspend fun processEvent(event: InputEvent, state: State): State =
        when {
            state == State.INITIAL && event is Init -> {
                outputCommands.emit(SetButtonSelection(ModeButtonCode.NO_FILTERS, true))
                outputCommands.emit(
                    SetInitialImage(
                        editorStorage.displayedImage,
                        filterSettings[GlFilterCode.ORIGINAL],
                        getFilterTitle(),
                        isMagicMode = false
                    ))
                State.MAIN
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.GL_FILTERS -> {
                editorStorage.onUpdate()
                outputCommands.emit(SetButtonSelection(ModeButtonCode.NO_FILTERS, false))
                State.GL_FILTERS
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.MAGIC -> {
                editorStorage.onUpdate()

                if(editorStorage.isSourceImageDisplayed) {
                    outputCommands.emit(SetButtonSelection(ModeButtonCode.MAGIC, true))
                    outputCommands.emit(SetProgressVisibility(true))
                    editorStorage.switchToHistogramEqualizedImage()
                    outputCommands.emit(SetProgressVisibility(false))

                    outputCommands.emit(
                        SetInitialImage(
                            editorStorage.displayedImage,
                            filterSettings[GlFilterCode.ORIGINAL],
                            getFilterTitle(),
                            isMagicMode = true
                        ))
                } else {
                    outputCommands.emit(SetButtonSelection(ModeButtonCode.MAGIC, false))
                    editorStorage.switchToSourceImage()
                    outputCommands.emit(
                        SetInitialImage(
                            editorStorage.displayedImage,
                            filterSettings[GlFilterCode.ORIGINAL],
                            getFilterTitle(),
                            isMagicMode = true
                        ))
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

            else -> state
        }

    @StringRes
    private fun getFilterTitle() = R.string.filterOriginal
}