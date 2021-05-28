package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.no_filters

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.*
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import kotlinx.coroutines.flow.MutableSharedFlow

class NoFiltersMachine(
    outputCommands: MutableSharedFlow<OutputCommand>,
    dispatchersProvider: DispatchersProvider
) : EditorMachineBase(outputCommands, dispatchersProvider) {

    override suspend fun processEvent(event: InputEvent, state: State): State =
        when {
            state == State.INITIAL && event is Init -> {
                outputCommands.emit(SelectButton(ModeButtonCode.NO_FILTERS))
                // todo Set the Original filter to an image (FilterSettingsFacadeImpl)
                State.MAIN
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.GL_FILTERS -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.NO_FILTERS))
                State.GL_FILTERS
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.CUT -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.NO_FILTERS))
                State.CUT
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.SYSTEM_FILTERS -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.NO_FILTERS))
                State.SYSTEM_FILTERS
            }

            state == State.MAIN && event is AcceptClicked -> {
                // todo Update image in a storage (if necessary)
                outputCommands.emit(CloseEditor)
                State.FINAL
            }

            state == State.MAIN && event is CancelClicked -> {
                State.CANCELING
            }

            else -> state
        }
}