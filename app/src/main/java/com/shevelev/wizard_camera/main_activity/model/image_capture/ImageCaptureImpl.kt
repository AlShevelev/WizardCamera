package com.shevelev.wizard_camera.main_activity.model.image_capture

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.MediaScannerConnection
import android.view.TextureView
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.camera.filter.FilterCode
import com.shevelev.wizard_camera.main_activity.dto.ScreenOrientation
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import io.golos.utils.id.IdUtil
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.*
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

    override suspend fun capture(textureView: TextureView, activeFilter: FilterCode, screenOrientation: ScreenOrientation): Boolean {
        inProgress = true
        try {
            val rawBitmap = textureView.bitmap

            val bitmap = withContext(dispatchersProvider.calculationsDispatcher) {
                correctScreenOrientation(rawBitmap, screenOrientation)
            }

            val outputFile = withContext(dispatchersProvider.ioDispatcher) {
                createFileForSaving(activeFilter).also { outputFile ->
                    FileOutputStream(outputFile).use { outputStream ->
                        compressBitmap(bitmap, outputStream)
                        outputStream.flush()
                    }
                }
            }

            MediaScannerConnection.scanFile(appContext, arrayOf<String>(outputFile.absolutePath), arrayOf("image/jpeg"), null)

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

    private fun compressBitmap(bitmap: Bitmap, outputStream: OutputStream) =
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream)

    private fun rotate(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun convertScreenOrientationToDegrees(orientation: ScreenOrientation): Float =
        when (orientation) {
            ScreenOrientation.PORTRAIT -> 0f
            ScreenOrientation.LANDSCAPE -> 270f
            ScreenOrientation.REVERSED_LANDSCAPE -> 90f
            ScreenOrientation.REVERSED_PORTRAIT -> 180f
        }

    private fun correctScreenOrientation(bitmap: Bitmap, screenOrientation: ScreenOrientation): Bitmap {
        val degrees = convertScreenOrientationToDegrees(screenOrientation)
        return if (degrees != 0f) {
            rotate(bitmap, degrees)
        } else {
            bitmap
        }
    }
}