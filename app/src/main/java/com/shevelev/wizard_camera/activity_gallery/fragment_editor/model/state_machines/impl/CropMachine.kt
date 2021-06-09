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
                outputCommands.emit(SelectButton(ModeButtonCode.CROP))
                outputCommands.emit(ShowCroppingImage)
                State.MAIN
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.NO_FILTERS -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.CROP))
                outputCommands.emit(HideCroppingImage)
                State.NO_FILTERS
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.GL_FILTERS -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.CROP))
                outputCommands.emit(HideCroppingImage)
                State.GL_FILTERS
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.CROP -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.CROP))
                outputCommands.emit(HideCroppingImage)
                State.CROP
            }

            state == State.MAIN && event is CancelClicked -> {
                outputCommands.emit(HideCroppingImage)
                State.PREVIOUS_MODE
            }

            state == State.MAIN && event is AcceptClicked -> {
                outputCommands.emit(HideCroppingImage)
                // todo update an original image
                State.PREVIOUS_MODE
            }

            else -> state
        }
}