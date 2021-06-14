package com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.storage

import android.graphics.Bitmap
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings

interface EditorStorage {
    val image: Bitmap

    var currentFilter: GlFilterSettings

    suspend fun decodeBitmap()
}