package com.shevelev.wizard_camera.core.photo_files.impl.photo_shot_repository.conventional.media_scanner

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume

internal class MediaScannerImpl(
    private val appContext: Context
) : MediaScanner {
    override suspend fun processNewShot(shot: File): Uri? =
        suspendCancellableCoroutine { continuation ->
            MediaScannerConnection.scanFile(appContext, arrayOf<String>(shot.absolutePath), arrayOf("image/jpeg")) { _, uri ->
                continuation.resume(uri)
            }
        }

    /**
     * @param resultCallback content uri of the file or null in case of error
     */
    override fun processNewShot(shot: File, resultCallback: (Uri?) -> Unit) =
        MediaScannerConnection.scanFile(appContext, arrayOf<String>(shot.absolutePath), arrayOf("image/jpeg")) { _, uri ->
            resultCallback(uri)
        }

    override fun processDeletedShot(shot: File) {
        MediaScannerConnection.scanFile(appContext, arrayOf<String>(shot.absolutePath), null, null)
    }
}