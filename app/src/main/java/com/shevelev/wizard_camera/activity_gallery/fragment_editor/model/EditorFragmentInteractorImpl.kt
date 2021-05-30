package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorage
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage.EditorStorageImpl
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.shared.files.FilesHelper
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EditorFragmentInteractorImpl
@Inject
constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val filesHelper: FilesHelper,
    private val photoShot: PhotoShot
) : EditorFragmentInteractor {

    private lateinit var editorStorage: EditorStorage

    override suspend fun init() {
        val tempFile = withContext(dispatchersProvider.ioDispatcher) {
            filesHelper.copyToTempFile(filesHelper.getShotFileByName(photoShot.fileName))
        }

        editorStorage = EditorStorageImpl(photoShot, tempFile)

        // Create and start the orchestrator here
    }
}