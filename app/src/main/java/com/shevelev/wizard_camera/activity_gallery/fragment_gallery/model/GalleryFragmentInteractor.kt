package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model

import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model.dto.ShotsLoadingResult
import com.shevelev.wizard_camera.shared.mvvm.model.InteractorBase
import kotlinx.coroutines.flow.Flow

interface GalleryFragmentInteractor : InteractorBase {
    val loadingResult: Flow<ShotsLoadingResult>

    val pageSize: Int

    suspend fun setUp()

    suspend fun loadPage()

    suspend fun delete(position: Int)

    fun getShot(position: Int): PhotoShot

    fun clear()
}