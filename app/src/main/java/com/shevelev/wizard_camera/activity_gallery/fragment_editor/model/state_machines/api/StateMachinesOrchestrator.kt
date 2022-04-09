package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api

import kotlinx.coroutines.flow.SharedFlow

interface StateMachinesOrchestrator {
    val commands: SharedFlow<OutputCommand>

    suspend fun processEvent(event: InputEvent)

    suspend fun start()
}