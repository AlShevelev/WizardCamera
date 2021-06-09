package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage

import android.graphics.Bitmap
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot

interface EditorStorage {
    val sourceShot: PhotoShot

    val image: Bitmap

    suspend fun decodeBitmap()
}