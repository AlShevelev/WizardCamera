package com.shevelev.wizard_camera.gallery_activity.model

import androidx.lifecycle.LiveData
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.shared.mvvm.model.ModelBase

interface GalleryActivityModel : ModelBase {
    val photos: LiveData<List<PhotoShot>>

    val pageSize: Int

    suspend fun loadPage(): Boolean
}