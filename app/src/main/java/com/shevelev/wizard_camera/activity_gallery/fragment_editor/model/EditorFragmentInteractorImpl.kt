package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.InitialMachine
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.InputEvent
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.OutputCommand
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.state_machines.api.StateMachinesOrchestrator
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorage
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorageImpl
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.shared.files.FilesHelper
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EditorFragmentInteractorImpl
@Inject
constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val filesHelper: FilesHelper,
    private val photoShot: PhotoShot,
    private val stateMachinesOrchestrator: StateMachinesOrchestrator
) : EditorFragmentInteractor {

    private lateinit var editorStorage: EditorStorage

    override val commands: SharedFlow<OutputCommand>
        get() = stateMachinesOrchestrator.commands

    override suspend fun init() {
        val tempFile = withContext(dispatchersProvider.ioDispatcher) {
            filesHelper.copyToTempFile(filesHelper.getShotFileByName(photoShot.fileName))
        }

        editorStorage = EditorStorageImpl(photoShot, tempFile)

        val initialMachine = if(photoShot.filter.code == GlFilterCode.ORIGINAL) {
            InitialMachine.NO_FILTERS
        } else {
            InitialMachine.GL_FILTERS
        }
        stateMachinesOrchestrator.start(initialMachine)
    }

    override suspend fun processEvent(event: InputEvent) {
        stateMachinesOrchestrator.processEvent(event)
    }
}