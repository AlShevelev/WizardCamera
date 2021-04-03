package com.shevelev.wizard_camera.main_activity.model.image_capture

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.main_activity.dto.ScreenOrientation
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.shared.files.FilesHelper
import com.shevelev.wizard_camera.shared.media_scanner.MediaScanner
import com.shevelev.wizard_camera.storage.repositories.PhotoShotRepository
import com.shevelev.wizard_camera.utils.id.IdUtil
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class ImageCaptureImpl
@Inject
constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val photoShotRepository: PhotoShotRepository,
    private val filesHelper: FilesHelper,
    private val mediaScanner: MediaScanner
) : ImageCapture {

    override val inProgress: Boolean
        get() = targetFile != null

    private var targetFile: File? = null

    private var activeFilter: FilterCode? = null

    /**
     * Starts capturing process
     * @return target file for an image
     */
    override suspend fun startCapture(activeFilter: FilterCode, screenOrientation: ScreenOrientation): File? {
        targetFile = try {
            this.activeFilter = activeFilter

            withContext(dispatchersProvider.ioDispatcher) {
                filesHelper.createFileForShot(activeFilter)
            }
        } catch (ex: Exception) {
            Timber.e(ex)
            null
        }

        return targetFile
    }

    override suspend fun captureCompleted() {
        withContext(dispatchersProvider.ioDispatcher) {
            targetFile?.let { targetFile ->
                checkAndCorrectOrientation(targetFile)

                val contentUri = mediaScanner.processNewShot(targetFile)

                saveToDb(targetFile.name, activeFilter!!, contentUri)
            }
        }

        targetFile = null
    }

    private fun checkAndCorrectOrientation(file: File) {
        val exifOrientation = ExifInterface(file)
            .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

        if(needToRotate(exifOrientation)) {
            saveBitmapToFile(rotateBitmap(BitmapFactory.decodeFile(file.absolutePath), exifOrientation), file)
        }
    }

    private fun needToRotate(exifOrientation: Int) =
        exifOrientation == ExifInterface.ORIENTATION_ROTATE_90 ||
            exifOrientation == ExifInterface.ORIENTATION_ROTATE_180 ||
            exifOrientation == ExifInterface.ORIENTATION_ROTATE_270

    private fun rotateBitmap(source: Bitmap, exifOrientation: Int): Bitmap {
        val rotateAngle = when(exifOrientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 ->180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }

        if(rotateAngle == 0) {
            return source
        }

        val matrix = Matrix()
        matrix.postRotate(rotateAngle.toFloat())

        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    private fun saveBitmapToFile(bitmap: Bitmap, targetFile: File) {
        targetFile.outputStream().use { fileOut ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, DEFAULT_COMPRESS_RATE, fileOut)
        }
    }

    private fun saveToDb(fileName: String, filter: FilterCode, contentUri: Uri?) =
        photoShotRepository.create(PhotoShot(IdUtil.generateLongId(), fileName, ZonedDateTime.now(), filter, contentUri))

    companion object {
        private const val DEFAULT_COMPRESS_RATE = 75
    }
}