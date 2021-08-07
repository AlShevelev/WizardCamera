package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model.image_importer

import android.net.Uri
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot

interface ImageImporter {
    suspend fun import(uri: Uri): PhotoShot?
}