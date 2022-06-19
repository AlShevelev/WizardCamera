package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.impl

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.*
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorage
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.core.common_entities.enums.FiltersGroup
import com.shevelev.wizard_camera.feature.filters_facade.api.FiltersFacade
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

internal class StateMachinesOrchestratorImpl (
    private val editorStorage: EditorStorage,
    private val filters: FiltersFacade
) : StateMachinesOrchestrator {
    private val _commands = MutableSharedFlow<OutputCommand>()
    override val commands = _commands.asSharedFlow()

    private val noFilterMachine by lazy {
        NoFiltersMachine(
            outputCommands = _commands,
            editorStorage = editorStorage,
            filters = filters,
            group = FiltersGroup.NO_FILTERS
        )
    }

    private val glFilterMachine by lazy {
        GlFiltersMachine(
            outputCommands = _commands,
            editorStorage = editorStorage,
            filters = filters
        )
    }

    private val cancelingMachine by lazy { CancelingMachine(_commands, editorStorage) }

    private var previousMachine: EditorMachineBase? = null
    
    private lateinit var activeMachine: EditorMachineBase

    override suspend fun processEvent(event: InputEvent) {
        when(activeMachine.processEvent(event)) {
            State.GL_FILTERS_NONE -> switchToMachine(noFilterMachine)

            State.GL_FILTERS_ALL -> switchToMachine(glFilterMachine, FiltersGroup.ALL)
            State.GL_FILTERS_COLORS -> switchToMachine(glFilterMachine, FiltersGroup.COLORS)
            State.GL_FILTERS_DEFORMATIONS -> switchToMachine(glFilterMachine, FiltersGroup.DEFORMATIONS)
            State.GL_FILTERS_STYLIZATION -> switchToMachine(glFilterMachine, FiltersGroup.STYLIZATION)
            State.GL_FILTERS_FAVORITES -> switchToMachine(glFilterMachine, FiltersGroup.FAVORITES)

            State.CANCELING -> switchToMachine(cancelingMachine)

            State.PREVIOUS_MODE -> switchToMachine(previousMachine!!)
            else -> {}
        }
    }

    override suspend fun start(sourceShot: PhotoShot) {
        val initialMachine = if(editorStorage.lastUsedGlFilter == null) {
            InitialMachine.NO_FILTERS
        } else {
            InitialMachine.GL_FILTERS
        }

        editorStorage.initImage(sourceShot)

        when (initialMachine) {
            InitialMachine.NO_FILTERS -> switchToMachine(noFilterMachine)
            InitialMachine.GL_FILTERS -> switchToMachine(glFilterMachine)
        }
    }

    private suspend fun switchToMachine(machine: EditorMachineBase) {
        if(::activeMachine.isInitialized) {
            previousMachine = activeMachine
        }
        
        activeMachine = machine
        activeMachine.resetState()
        activeMachine.processEvent(Init)
    }

    private suspend fun switchToMachine(machine: EditorMachineBase, group: FiltersGroup) {
        if(::activeMachine.isInitialized) {
            previousMachine = activeMachine
        }

        activeMachine = machine
        activeMachine.resetState()
        activeMachine.processEvent(InitGlFiltersMachine(group))
    }
}