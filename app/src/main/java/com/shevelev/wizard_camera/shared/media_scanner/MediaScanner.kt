package com.shevelev.wizard_camera.shared.media_scanner

import android.net.Uri
import java.io.File

interface MediaScanner {
    /**
     * @return content uri of the file or null in case of error
     */
    suspend fun processNewShot(shot: File): Uri?

    fun processDeletedShot(shot: File)
}