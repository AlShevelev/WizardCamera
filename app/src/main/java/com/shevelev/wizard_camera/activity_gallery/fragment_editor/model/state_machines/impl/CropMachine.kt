package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.impl

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.*
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorage
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import kotlinx.coroutines.flow.MutableSharedFlow

class CropMachine(
    outputCommands: MutableSharedFlow<OutputCommand>,
    dispatchersProvider: DispatchersProvider,
    editorStorage: EditorStorage
) : EditorMachineBase(outputCommands, dispatchersProvider, editorStorage) {

    override suspend fun processEvent(event: InputEvent, state: State): State =
        when {
            state == State.INITIAL && event is Init -> {
                outputCommands.emit(SetButtonSelection(ModeButtonCode.CROP, true))
                outputCommands.emit(SetCroppingImageVisibility(true))
                State.MAIN
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.NO_FILTERS -> {
                outputCommands.emit(SetButtonSelection(ModeButtonCode.CROP, false))
                outputCommands.emit(SetCroppingImageVisibility(false))
                State.NO_FILTERS
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.GL_FILTERS -> {
                outputCommands.emit(SetButtonSelection(ModeButtonCode.CROP, false))
                outputCommands.emit(SetCroppingImageVisibility(false))
                State.GL_FILTERS
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.CROP -> {
                outputCommands.emit(SetButtonSelection(ModeButtonCode.CROP, false))
                outputCommands.emit(SetCroppingImageVisibility(false))
                State.CROP
            }

            state == State.MAIN && event is CancelClicked -> {
                outputCommands.emit(SetCroppingImageVisibility(false))
                State.PREVIOUS_MODE
            }

            state == State.MAIN && event is AcceptClicked -> {
                outputCommands.emit(SetCroppingImageVisibility(false))
                // todo update an original image
                State.PREVIOUS_MODE
            }

            else -> state
        }
}