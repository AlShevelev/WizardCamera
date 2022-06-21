package com.shevelev.wizard_camera.activity_gallery.shared.data_pass

import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot

internal interface FragmentsDataPass {
    fun putPhotoShot(photoShot: PhotoShot)

    /**
     *  Gets an entity from storage and then removes the entity from the storage
     */
    fun extractPhotoShot(): PhotoShot?
}