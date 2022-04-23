package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api

import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import kotlinx.coroutines.flow.SharedFlow

interface StateMachinesOrchestrator {
    val commands: SharedFlow<OutputCommand>

    suspend fun processEvent(event: InputEvent)

    suspend fun start(sourceShot: PhotoShot)
}