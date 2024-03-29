package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.InputEvent
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.OutputCommand
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.core.ui_kit.lib.flower_menu.FlowerMenuItemData
import com.shevelev.wizard_camera.core.ui_utils.mvvm.model.InteractorBase
import kotlinx.coroutines.flow.SharedFlow

interface EditorFragmentInteractor : InteractorBase {
    val commands: SharedFlow<OutputCommand>

    suspend fun init(sourceShot: PhotoShot)

    suspend fun processEvent(event: InputEvent)

    fun getFiltersForMenu(): List<FlowerMenuItemData>
}