package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.InputEvent
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.OutputCommand
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.StateMachinesOrchestrator
import com.shevelev.wizard_camera.feature.filters_facade.api.FiltersFacade
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.core.ui_kit.lib.flower_menu.FlowerMenuItemData
import kotlinx.coroutines.flow.SharedFlow

class EditorFragmentInteractorImpl(
    private val stateMachinesOrchestrator: StateMachinesOrchestrator,
    private val filters: FiltersFacade
) : EditorFragmentInteractor {

    override val commands: SharedFlow<OutputCommand>
        get() = stateMachinesOrchestrator.commands

    override suspend fun init(sourceShot: PhotoShot) {
        filters.init()
        stateMachinesOrchestrator.start(sourceShot)
    }

    override suspend fun processEvent(event: InputEvent) {
        stateMachinesOrchestrator.processEvent(event)
    }

    override fun getFiltersForMenu(): List<FlowerMenuItemData> = filters.getFiltersForMenu()
}
