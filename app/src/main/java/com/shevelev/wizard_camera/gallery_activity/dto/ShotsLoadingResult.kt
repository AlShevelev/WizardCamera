package com.shevelev.wizard_camera.gallery_activity.dto

import com.shevelev.wizard_camera.common_entities.entities.PhotoShot

sealed class ShotsLoadingResult {
    object PreLoading : ShotsLoadingResult()
    data class DataUpdated(val data: List<PhotoShot>) : ShotsLoadingResult()
}