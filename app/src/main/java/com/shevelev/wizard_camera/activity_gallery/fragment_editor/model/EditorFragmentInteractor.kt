package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.InputEvent
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.OutputCommand
import com.shevelev.wizard_camera.shared.mvvm.model.InteractorBase
import kotlinx.coroutines.flow.SharedFlow

interface EditorFragmentInteractor : InteractorBase {
    val commands: SharedFlow<OutputCommand>

    suspend fun init()

    suspend fun processEvent(event: InputEvent)
}