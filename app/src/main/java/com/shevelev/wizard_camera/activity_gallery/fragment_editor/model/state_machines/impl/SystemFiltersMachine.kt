package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.impl

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.*
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorage
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import kotlinx.coroutines.flow.MutableSharedFlow

class SystemFiltersMachine(
    outputCommands: MutableSharedFlow<OutputCommand>,
    dispatchersProvider: DispatchersProvider,
    editorStorage: EditorStorage
) : EditorMachineBase(outputCommands, dispatchersProvider, editorStorage) {

    override suspend fun processEvent(event: InputEvent, state: State): State =
        when {
            state == State.INITIAL && event is Init -> {
                outputCommands.emit(SelectButton(ModeButtonCode.SYSTEM_FILTERS))
                outputCommands.emit(ShowSystemFilterCarousel)
                // todo Scroll to the last selected filter (or the first one)
                // todo Show the settings in a last position (or a neutral one)
                // todo Use the filter on an original image
                State.MAIN
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.NO_FILTERS -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.SYSTEM_FILTERS))
                outputCommands.emit(HideSystemFilterCarousel)
                State.NO_FILTERS
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.GL_FILTERS -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.SYSTEM_FILTERS))
                outputCommands.emit(HideSystemFilterCarousel)
                State.GL_FILTERS
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.CROP -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.SYSTEM_FILTERS))
                outputCommands.emit(HideSystemFilterCarousel)
                State.CROP
            }

            state == State.MAIN && event is CancelClicked -> {
                State.CANCELING
            }

            state == State.MAIN && event is AcceptClicked -> {
                // todo Update image in a storage (if necessary)
                outputCommands.emit(CloseEditor)
                State.FINAL
            }

            state == State.MAIN && event is SystemFilterSettingsUpdated -> {
                // todo Memorize the settings
                // todo Use new filter to an image
                state
            }

            state == State.MAIN && event is SystemFilterSwitched -> {
                // todo Memorize the filter
                // todo Use new filter to an image
                state
            }

            else -> state
        }
}