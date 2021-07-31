package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model

import android.graphics.Bitmap
import android.net.Uri
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.dto.ShotsLoadingResult
import com.shevelev.wizard_camera.shared.mvvm.model.InteractorBase
import kotlinx.coroutines.flow.Flow

interface GalleryFragmentInteractor : InteractorBase {
    val loadingResult: Flow<ShotsLoadingResult>

    val pageSize: Int

    suspend fun initPhotos()

    suspend fun loadNextPage()

    suspend fun delete(position: Int)

    fun getShot(position: Int): PhotoShot

    fun clear()

    /**
     * Saves a bitmap into a temporary file and returns content Uri for sharing
     */
    suspend fun startBitmapSharing(bitmap: Bitmap): Uri
}