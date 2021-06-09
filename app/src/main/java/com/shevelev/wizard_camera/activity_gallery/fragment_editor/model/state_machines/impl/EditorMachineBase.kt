package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.impl

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.InputEvent
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.OutputCommand
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.impl.State
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorage
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Base class for all editor State Machines
 */
abstract class EditorMachineBase(
    protected val outputCommands: MutableSharedFlow<OutputCommand>,
    protected val dispatchersProvider: DispatchersProvider,
    protected val editorStorage: EditorStorage
) {
    private var state = State.INITIAL

    /**
     * Handles user event
     * @param event event to process
     * @return result state
     */
    suspend fun processEvent(event: InputEvent): State {
        state = processEvent(event, state)
        return state
    }

    /**
     * Moves the SM to Initial state
     */
    fun resetState() {
        state = State.INITIAL
    }

    protected abstract suspend fun processEvent(event: InputEvent, state: State): State
}