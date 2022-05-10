package com.shevelev.wizard_camera.core.photo_files.impl.photo_shot_repository.conventional.media_scanner

import android.net.Uri
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import java.io.File

interface MediaScanner {
    /**
     * @return content uri of the file or null in case of error
     */
    suspend fun processNewShot(shot: File): Uri?

    /**
     * @param resultCallback content uri of the file or null in case of error
     */
    fun processNewShot(shot: File, resultCallback: (Uri?) -> Unit)

    fun processDeletedShot(shot: File)
}