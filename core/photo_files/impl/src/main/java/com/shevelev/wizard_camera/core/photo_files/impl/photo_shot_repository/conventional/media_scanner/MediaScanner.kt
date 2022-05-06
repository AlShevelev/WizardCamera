package com.shevelev.wizard_camera.core.photo_files.impl.photo_shot_repository.conventional.media_scanner

import android.net.Uri
import java.io.File

interface MediaScanner {
    /**
     * @return content uri of the file or null in case of error
     */
    suspend fun processNewShot(shot: File): Uri?

    fun processDeletedShot(shot: File)
}