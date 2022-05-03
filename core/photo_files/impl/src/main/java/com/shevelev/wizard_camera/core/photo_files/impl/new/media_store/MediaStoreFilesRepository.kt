package com.shevelev.wizard_camera.core.photo_files.impl.new.media_store

import android.graphics.Bitmap
import android.net.Uri
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.photo_files.api.new.PhotoShotRepository
import java.io.OutputStream

/**
 * An new-style repository, based on MediaStore
 */
internal class MediaStoreFilesRepository() : PhotoShotRepository {
    /**
     * Creates a file for a photo shot and returns its OutputStream
     */
    override suspend fun startCapturing(): OutputStream {
        TODO("Not yet implemented")
        // create a record inside MediaStorage
        // (see [1] line 83)
        // create an output stream via content resolver
        // store the Uri in Map<OutputStream, Uri>
        // return the output stream
    }

    /**
     * Completes capturing process
     * @param stream a stream created in [startCapturing] function
     */
    override suspend fun completeCapturing(stream: OutputStream, filter: GlFilterSettings): PhotoShot? {
        TODO("Not yet implemented")
        // close the stream
        // rotate an image if we need it
        // update the database
        // reset a pending flag (see [1] line 93-95)
        // remove a record from the Map<OutputStream, Uri>
    }

    /**
     * Saves a bitmap into a temporary storage and returns content Uri for the saved bitmap
     */
    override suspend fun saveBitmapToTempStorage(bitmap: Bitmap): Uri {
        TODO("Not yet implemented")
    }

    /**
     * Removes a given shot
     */
    override suspend fun removeShot(photoShot: PhotoShot) {
        TODO("Not yet implemented")
    }

    /**
     * Updates a given shot by a new bitmap or/and a new filter
     * @return updated shot
     */
    override suspend fun updateShot(bitmap: Bitmap, filter: GlFilterSettings, updatedShot: PhotoShot): PhotoShot {
        TODO("Not yet implemented")
    }
}

// [1] https://gitlab.com/commonsguy/cw-android-q/-/blob/vFINAL/ConferenceVideos/src/main/java/com/commonsware/android/conferencevideos/VideoRepository.kt