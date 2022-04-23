package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.InputEvent
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.OutputCommand
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.StateMachinesOrchestrator
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import kotlinx.coroutines.flow.SharedFlow

class EditorFragmentInteractorImpl
constructor(
    private val stateMachinesOrchestrator: StateMachinesOrchestrator
) : EditorFragmentInteractor {

    override val commands: SharedFlow<OutputCommand>
        get() = stateMachinesOrchestrator.commands

    override suspend fun init(sourceShot: PhotoShot) = stateMachinesOrchestrator.start(sourceShot)

    override suspend fun processEvent(event: InputEvent) {
        stateMachinesOrchestrator.processEvent(event)
    }
}