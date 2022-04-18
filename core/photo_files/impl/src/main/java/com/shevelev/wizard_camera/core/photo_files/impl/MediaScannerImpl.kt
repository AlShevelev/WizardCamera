package com.shevelev.wizard_camera.core.photo_files.impl

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import java.io.File
import javax.inject.Inject

class MediaScannerImpl
@Inject
constructor(
    private val appContext: Context
) : com.shevelev.wizard_camera.core.photo_files.api.MediaScanner {
    override suspend fun processNewShot(shot: File): Uri? {
        return suspendCancellableCoroutine { continuation ->
            MediaScannerConnection.scanFile(appContext, arrayOf<String>(shot.absolutePath), arrayOf("image/jpeg")) { _, uri ->
                continuation.resume(uri)
            }
        }
    }

    override fun processDeletedShot(shot: File) {
        MediaScannerConnection.scanFile(appContext, arrayOf<String>(shot.absolutePath), null, null)
    }
}