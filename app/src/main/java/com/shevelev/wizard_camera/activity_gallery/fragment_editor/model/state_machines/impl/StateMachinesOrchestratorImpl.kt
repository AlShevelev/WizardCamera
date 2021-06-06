package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.impl

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.*
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class StateMachinesOrchestratorImpl
@Inject
constructor(
    dispatchersProvider: DispatchersProvider
) : StateMachinesOrchestrator {
    private val _commands = MutableSharedFlow<OutputCommand>()
    override val commands = _commands.asSharedFlow()

    private val noFilterMachine by lazy { NoFiltersMachine(_commands, dispatchersProvider) }
    private val glFilterMachine by lazy { GlFiltersMachine(_commands, dispatchersProvider) }
    private val systemFilterMachine by lazy { SystemFiltersMachine(_commands, dispatchersProvider) }
    private val cropMachine by lazy { CropMachine(_commands, dispatchersProvider) }
    private val cancelingMachine by lazy { CancelingMachine(_commands, dispatchersProvider) }

    private var previousMachine: EditorMachineBase? = null
    
    private lateinit var activeMachine: EditorMachineBase

    override suspend fun processEvent(event: InputEvent) {
        when(activeMachine.processEvent(event)) {
            State.NO_FILTERS -> switchToMachine(noFilterMachine)
            State.GL_FILTERS -> switchToMachine(glFilterMachine)
            State.SYSTEM_FILTERS -> switchToMachine(systemFilterMachine)
            State.CROP -> switchToMachine(cropMachine)
            State.CANCELING -> switchToMachine(cancelingMachine)
            State.PREVIOUS_MODE -> switchToMachine(previousMachine!!)
            else -> {}
        }

        activeMachine.processEvent(event)
    }

    override suspend fun start(initialMachine: InitialMachine) =
        when(initialMachine) {
            InitialMachine.NO_FILTERS -> switchToMachine(noFilterMachine)
            InitialMachine.GL_FILTERS -> switchToMachine(glFilterMachine)
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