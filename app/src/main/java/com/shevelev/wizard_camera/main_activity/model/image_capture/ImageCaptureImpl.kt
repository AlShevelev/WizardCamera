package com.shevelev.wizard_camera.main_activity.model.image_capture

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.view.TextureView
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.camera.filter.FilterCode
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import io.golos.utils.id.IdUtil
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import kotlin.math.absoluteValue

class ImageCaptureImpl
@Inject
constructor(
    private val appContext: Context,
    private val dispatchersProvider: DispatchersProvider
) : ImageCapture {

    override var inProgress: Boolean = false
        private set

    override suspend fun capture(textureView: TextureView, activeFilter: FilterCode): Boolean {
        inProgress = true
        try {
            val fileForSaving = withContext(dispatchersProvider.ioDispatcher) {
                createFileForSaving(activeFilter)
            }

            val bitmap = textureView.bitmap

            withContext(dispatchersProvider.ioDispatcher) {
                FileOutputStream(fileForSaving).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream)
                    outputStream.flush()
                }
            }

            MediaScannerConnection.scanFile(appContext, arrayOf<String>(fileForSaving.absolutePath), arrayOf("image/jpeg"), null)

            return true
        } catch (ex: Exception) {
            Timber.e(ex)
            return false
        } finally {
            inProgress = false
        }
    }

    private fun createFileForSaving(activeFilter: FilterCode): File {
        val prefix = activeFilter.toString().toLowerCase(Locale.getDefault())
        val suffix = IdUtil.generateLongId().absoluteValue

        val dir = File(appContext.externalMediaDirs[0], appContext.getString(R.string.appName))
        if(!dir.exists()) {
            dir.mkdir()
        }

        return File(dir, "$prefix$suffix.jpg")
    }
}