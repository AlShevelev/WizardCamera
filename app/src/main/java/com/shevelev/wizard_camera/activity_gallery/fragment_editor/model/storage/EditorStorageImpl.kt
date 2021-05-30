package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage

import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import java.io.File

/**
 * @property tempImage a copy of an image from [sourceShot]
 */
class EditorStorageImpl
constructor(
    private val sourceShot: PhotoShot,
    private val tempImage: File
) : EditorStorage {
}