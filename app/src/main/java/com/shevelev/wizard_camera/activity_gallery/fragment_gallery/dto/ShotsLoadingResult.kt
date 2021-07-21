package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.dto

sealed class ShotsLoadingResult {
    object PreLoading : ShotsLoadingResult()
    data class DataUpdated(val data: List<GalleryItem>) : ShotsLoadingResult()
}