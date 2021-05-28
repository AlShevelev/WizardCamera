package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.cutting

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.*
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import kotlinx.coroutines.flow.MutableSharedFlow

class CutFilterMachine(
    outputCommands: MutableSharedFlow<OutputCommand>,
    dispatchersProvider: DispatchersProvider
) : EditorMachineBase(outputCommands, dispatchersProvider) {

    override suspend fun processEvent(event: InputEvent, state: State): State =
        when {
            state == State.INITIAL && event is Init -> {
                outputCommands.emit(SelectButton(ModeButtonCode.CUT))
                outputCommands.emit(ShowOriginalImage)
                outputCommands.emit(ShowCuttingGrid)
                State.MAIN
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.NO_FILTERS -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.CUT))
                outputCommands.emit(HideCuttingGrid)
                outputCommands.emit(HideOriginalImage)
                State.NO_FILTERS
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.GL_FILTERS -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.CUT))
                outputCommands.emit(HideCuttingGrid)
                outputCommands.emit(HideOriginalImage)
                State.GL_FILTERS
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.CUT -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.CUT))
                outputCommands.emit(HideCuttingGrid)
                outputCommands.emit(HideOriginalImage)
                State.CUT
            }

            state == State.MAIN && event is CancelClicked -> {
                outputCommands.emit(HideCuttingGrid)
                outputCommands.emit(HideOriginalImage)
                State.PREVIOUS_MODE
            }

            state == State.MAIN && event is AcceptClicked -> {
                outputCommands.emit(HideCuttingGrid)
                outputCommands.emit(HideOriginalImage)
                // todo update an original image
                State.PREVIOUS_MODE
            }

            state == State.MAIN && event is CuttingGridUpdated -> {
                // todo Memorize the grid size and position
                state
            }

            else -> state
        }
}