package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.dto

import android.os.Parcelable
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GalleryItem(
    val id: Long,
    val version: Int,
    val item: PhotoShot
) : Parcelable
