package com.shevelev.wizard_camera.bitmap_images.bitmap_utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.OutputStream
import javax.inject.Inject

class BitmapHelperImpl
@Inject
constructor(
    private val appContext: Context
) : BitmapHelper {
    override fun saveBitmap(file: File, bitmap: Bitmap) {
        file.outputStream().use {
            compressBitmap(bitmap, it)
        }
    }

    override fun saveBitmap(file: File, uri: Uri) {
        appContext.contentResolver.openInputStream(uri).use { input ->
            val markSupported = input!!.markSupported()

            val exifOrientation = if(markSupported) {
                input.mark(Int.MAX_VALUE)
                ExifInterface(input).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
            } else {
                ExifInterface.ORIENTATION_UNDEFINED
            }

            if(markSupported) {
                input.reset()
            }

            if(!needToRotate(exifOrientation)) {
                file.outputStream().use { fileOut ->
                    input.copyTo(fileOut)
                }
            } else {
                saveBitmap(file, rotateBitmap(BitmapFactory.decodeStream(input), exifOrientation))
            }
        }
    }

    override fun checkAndCorrectOrientation(file: File) {
        val exifOrientation = ExifInterface(file)
            .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

        if(needToRotate(exifOrientation)) {
            saveBitmap(file, rotateBitmap(loadBitmap(file), exifOrientation))
        }
    }

    private fun compressBitmap(bitmap: Bitmap, output: OutputStream) {
        // Jpeg format with "95" quality value is used - as same as ImageCapture settings
        // See [CameraManager.bindCameraUseCases]
        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, output)
    }

    private fun needToRotate(exifOrientation: Int) =
        exifOrientation == ExifInterface.ORIENTATION_ROTATE_90 ||
            exifOrientation == ExifInterface.ORIENTATION_ROTATE_180 ||
            exifOrientation == ExifInterface.ORIENTATION_ROTATE_270

    private fun loadBitmap(file: File): Bitmap = BitmapFactory.decodeFile(file.absolutePath)

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
}