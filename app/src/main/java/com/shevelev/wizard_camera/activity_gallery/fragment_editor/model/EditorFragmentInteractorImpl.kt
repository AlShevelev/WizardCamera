package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.InputEvent
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.OutputCommand
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.StateMachinesOrchestrator
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class EditorFragmentInteractorImpl
@Inject
constructor(
    private val stateMachinesOrchestrator: StateMachinesOrchestrator
) : EditorFragmentInteractor {

    override val commands: SharedFlow<OutputCommand>
        get() = stateMachinesOrchestrator.commands

    override suspend fun init() = stateMachinesOrchestrator.start()

    override suspend fun processEvent(event: InputEvent) {
        stateMachinesOrchestrator.processEvent(event)
    }
}