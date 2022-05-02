package com.shevelev.wizard_camera.core.photo_files.api.new

import android.graphics.Bitmap
import android.net.Uri
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import java.io.OutputStream

/**
 * It is an interface for a repository for files with photos
 */
interface PhotoFilesRepository {
    /**
     * Creates a file for a photo shot and returns its OutputStream
     */
    suspend fun startCapturing(): OutputStream

    /**
     * Completes capturing process
     * @param stream a stream created in [startCapturing] function
     */
    suspend fun completeCapturing(stream: OutputStream, filter: GlFilterSettings): PhotoShot?

    /**
     * Saves a bitmap into a temporary storage and returns content Uri for the saved bitmap
     */
    suspend fun saveBitmapToTempStorage(bitmap: Bitmap): Uri
}