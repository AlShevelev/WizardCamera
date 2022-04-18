package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.impl

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.*
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorage
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.settings.FilterSettingsFacade
import com.shevelev.wizard_camera.filters.display_data.gl.FilterDisplayDataList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class StateMachinesOrchestratorImpl
@Inject
constructor(
    private val editorStorage: EditorStorage,
    private val filterDisplayData: FilterDisplayDataList,
    private val filterSettings: FilterSettingsFacade
) : StateMachinesOrchestrator {
    private val _commands = MutableSharedFlow<OutputCommand>()
    override val commands = _commands.asSharedFlow()

    private val noFilterMachine by lazy {
        NoFiltersMachine(
            _commands,
            editorStorage,
            filterSettings
        )
    }

    private val glFilterMachine by lazy {
        GlFiltersMachine(
            _commands,
            editorStorage,
            filterDisplayData,
            filterSettings
        )
    }

    private val cancelingMachine by lazy { CancelingMachine(_commands, editorStorage) }

    private var previousMachine: EditorMachineBase? = null
    
    private lateinit var activeMachine: EditorMachineBase

    override suspend fun processEvent(event: InputEvent) {
        when(activeMachine.processEvent(event)) {
            State.NO_FILTERS -> switchToMachine(noFilterMachine)
            State.GL_FILTERS -> switchToMachine(glFilterMachine)
            State.CANCELING -> switchToMachine(cancelingMachine)
            State.PREVIOUS_MODE -> switchToMachine(previousMachine!!)
            else -> {}
        }
    }

    override suspend fun start() {
        val initialMachine = if(editorStorage.lastUsedGlFilter == null) {
            InitialMachine.NO_FILTERS
        } else {
            InitialMachine.GL_FILTERS
        }

        editorStorage.initImage()

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
}