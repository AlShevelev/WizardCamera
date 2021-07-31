package com.shevelev.wizard_camera.shared.bitmap

import android.graphics.Bitmap
import java.io.File
import java.io.OutputStream
import javax.inject.Inject

class BitmapHelperImpl
@Inject
constructor() : BitmapHelper {
    override fun saveBitmap(file: File, bitmap: Bitmap) {
        file.outputStream().use {
            compressBitmap(bitmap, it)
        }
    }

    private fun compressBitmap(bitmap: Bitmap, output: OutputStream) {
        // Jpeg format with "95" quality value is used - as same as ImageCapture settings
        // See [CameraManager.bindCameraUseCases]
        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, output)
    }
}