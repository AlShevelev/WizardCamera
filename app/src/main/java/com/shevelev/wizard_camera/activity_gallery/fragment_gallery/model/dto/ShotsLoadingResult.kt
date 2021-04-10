package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model.dto

import com.shevelev.wizard_camera.common_entities.entities.PhotoShot

sealed class ShotsLoadingResult {
    object PreLoading : ShotsLoadingResult()
    data class DataUpdated(val data: List<PhotoShot>) : ShotsLoadingResult()
}