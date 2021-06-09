package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.shared.files.FilesHelper
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @property sourceShot an initial shot
 */
class EditorStorageImpl
@Inject
constructor(
    private val dispatchersProvider: DispatchersProvider,
    override val sourceShot: PhotoShot,
    private val filesHelper: FilesHelper,
) : EditorStorage {
    override lateinit var image: Bitmap

    override suspend fun decodeBitmap() {
        image = withContext(dispatchersProvider.ioDispatcher) {
            BitmapFactory.decodeFile(filesHelper.getShotFileByName(sourceShot.fileName).absolutePath)
        }
    }
}