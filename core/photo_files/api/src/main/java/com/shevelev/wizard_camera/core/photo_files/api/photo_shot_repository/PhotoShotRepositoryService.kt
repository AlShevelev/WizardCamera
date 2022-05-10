package com.shevelev.wizard_camera.core.photo_files.api.photo_shot_repository

import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.photo_files.api.photo_shot_repository.model.StartCapturingResult

interface PhotoShotRepositoryService {
    /**
     * Completes capturing process (without coroutines, in a background thread)
     * @param key a value of [StartCapturingResult.key]
     */
    fun completeCapturingForService(key: Long, filter: GlFilterSettings)
}