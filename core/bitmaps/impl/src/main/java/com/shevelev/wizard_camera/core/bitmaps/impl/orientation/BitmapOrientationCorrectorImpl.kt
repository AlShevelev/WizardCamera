package com.shevelev.wizard_camera.core.bitmaps.impl.orientation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import com.shevelev.wizard_camera.core.bitmaps.api.orientation.BitmapOrientationCorrector
import androidx.exifinterface.media.ExifInterface
import com.shevelev.wizard_camera.core.bitmaps.api.orientation.Orientation
import java.io.File
import java.io.InputStream

internal class BitmapOrientationCorrectorImpl(
    private val appContext: Context
) : BitmapOrientationCorrector {
    override fun getOrientation(file: File): Orientation? =
        ExifInterface(file)
            .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
            .let { fromExif(it) }

    override fun getOrientation(uri: Uri): Orientation? =
        appContext.contentResolver.openInputStream(uri)?.use { stream ->
            ExifInterface(stream)
                .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
                .let { fromExif(it) }
        }

    override fun rotate(bitmap: Bitmap, orientation: Orientation): Bitmap {
        val rotateAngle = when(toExif(orientation)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 ->180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }

        if(rotateAngle == 0) {
            return bitmap
        }

        val matrix = Matrix()
        matrix.postRotate(rotateAngle.toFloat())

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun fromExif(exifOrientation: Int): Orientation? =
        when(exifOrientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> Orientation.ROTATE_RIGHT
            ExifInterface.ORIENTATION_ROTATE_270 -> Orientation.ROTATE_LEFT
            ExifInterface.ORIENTATION_ROTATE_180 -> Orientation.TURNED
            ExifInterface.ORIENTATION_NORMAL -> Orientation.NORMAL
            else -> null
        }

    private fun toExif(orientation: Orientation?): Int =
        when(orientation) {
            Orientation.ROTATE_RIGHT -> ExifInterface.ORIENTATION_ROTATE_90
            Orientation.ROTATE_LEFT -> ExifInterface.ORIENTATION_ROTATE_270
            Orientation.TURNED -> ExifInterface.ORIENTATION_ROTATE_180
            Orientation.NORMAL -> ExifInterface.ORIENTATION_NORMAL
            else -> ExifInterface.ORIENTATION_UNDEFINED
        }
}