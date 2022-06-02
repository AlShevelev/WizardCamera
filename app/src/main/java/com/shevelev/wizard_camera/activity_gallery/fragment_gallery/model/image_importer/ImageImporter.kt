package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model.image_importer

import android.net.Uri
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot

internal interface ImageImporter {
    suspend fun import(uri: Uri): PhotoShot?
}