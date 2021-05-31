package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.impl

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.*
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import kotlinx.coroutines.flow.MutableSharedFlow

class GlFiltersMachine(
    outputCommands: MutableSharedFlow<OutputCommand>,
    dispatchersProvider: DispatchersProvider
) : EditorMachineBase(outputCommands, dispatchersProvider) {

    override suspend fun processEvent(event: InputEvent, state: State): State =
        when {
            state == State.INITIAL && event is Init -> {
                outputCommands.emit(SelectButton(ModeButtonCode.GL_FILTERS))
                outputCommands.emit(ShowGlFilterCarousel)
                // todo Scroll the carousel to a last selected filter
                // todo Use the filter on an image
                State.MAIN
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.NO_FILTERS -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.GL_FILTERS))
                outputCommands.emit(HideGlFilterCarousel)
                State.NO_FILTERS
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.SYSTEM_FILTERS -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.GL_FILTERS))
                outputCommands.emit(HideGlFilterCarousel)
                State.SYSTEM_FILTERS
            }

            state == State.MAIN && event is ModeButtonClicked && event.code == ModeButtonCode.CROP -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.GL_FILTERS))
                outputCommands.emit(HideGlFilterCarousel)
                State.CROP
            }

            state == State.MAIN && event is CancelClicked -> {
                State.CANCELING
            }

            state == State.MAIN && event is GlFilterSettingsStarted -> {
                outputCommands.emit(HideGlFilterCarousel)
                // todo outputCommands.emit(ShowGlFilterSettings(...))      // Show settings
                State.SETTINGS_VISIBLE
            }

            state == State.MAIN && event is AcceptClicked -> {
                // todo Update image in a storage (if necessary)
                outputCommands.emit(CloseEditor)
                State.FINAL
            }

            state == State.MAIN && event is GlFilterSwitched -> {
                // todo Memorize the filter
                // todo Use new filter to an image
                state
            }

            state == State.SETTINGS_VISIBLE && event is ModeButtonClicked && event.code == ModeButtonCode.NO_FILTERS -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.GL_FILTERS))
                outputCommands.emit(HideGlFilterSettings)
                State.NO_FILTERS
            }

            state == State.SETTINGS_VISIBLE && event is ModeButtonClicked && event.code == ModeButtonCode.SYSTEM_FILTERS -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.GL_FILTERS))
                outputCommands.emit(HideGlFilterSettings)
                State.SYSTEM_FILTERS
            }

            state == State.SETTINGS_VISIBLE && event is ModeButtonClicked && event.code == ModeButtonCode.CROP -> {
                outputCommands.emit(UnSelectButton(ModeButtonCode.GL_FILTERS))
                outputCommands.emit(HideGlFilterSettings)
                State.CROP
            }

            state == State.SETTINGS_VISIBLE && event is GlFilterSettingsEnded -> {
                outputCommands.emit(HideGlFilterSettings)
                outputCommands.emit(ShowGlFilterCarousel)
                State.MAIN
            }

            state == State.SETTINGS_VISIBLE && event is AcceptClicked -> {
                // todo Update image in a storage (if necessary)
                outputCommands.emit(CloseEditor)
                State.FINAL
            }

            state == State.SETTINGS_VISIBLE && event is CancelClicked -> {
                State.CANCELING
            }

            state == State.SETTINGS_VISIBLE && event is GlFilterSettingsUpdated -> {
                // todo Memorize the settings
                // todo Update an image
                state
            }

            else -> state
        }
}