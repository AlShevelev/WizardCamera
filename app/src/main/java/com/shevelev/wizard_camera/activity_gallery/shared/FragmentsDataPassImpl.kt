package com.shevelev.wizard_camera.activity_gallery.shared

import com.shevelev.wizard_camera.core.common_entities.di_scopes.ActivityScope
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.shared.fragment_data_pass.FragmentsDataPushBase
import javax.inject.Inject

@ActivityScope
class FragmentsDataPassImpl
@Inject
constructor() : FragmentsDataPushBase(), FragmentsDataPass {
    override fun putPhotoShot(photoShot: PhotoShot) = put(PHOTO_SHOT, photoShot)

    /**
     *  Gets an entity from storage and then removes the entity from the storage
     */
    override fun extractPhotoShot(): PhotoShot? = extract(PHOTO_SHOT) as? PhotoShot

    companion object {
        private const val PHOTO_SHOT = 42
    }
}