package com.shevelev.wizard_camera.core.photo_files.api.photo_shot_repository.model

import java.io.OutputStream

data class StartCapturingResult(
    val key: Long,
    val capturingStream: OutputStream
)