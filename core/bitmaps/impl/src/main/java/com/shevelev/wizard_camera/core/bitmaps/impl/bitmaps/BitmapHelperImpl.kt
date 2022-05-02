package com.shevelev.wizard_camera.core.bitmaps.impl.bitmaps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.shevelev.wizard_camera.core.bitmaps.api.bitmaps.BitmapHelper
import java.io.File
import java.io.OutputStream

internal class BitmapHelperImpl
constructor(
    private val appContext: Context
) : BitmapHelper {
    override fun save(file: File, bitmap: Bitmap) {
        file.outputStream().use {
            compressBitmap(bitmap, it)
        }
    }

    override fun save(file: File, uri: Uri) {
        appContext.contentResolver.openInputStream(uri).use { input ->
            file.outputStream().use { fileOut ->
                input!!.copyTo(fileOut)
            }
        }
    }

    override fun save(stream: OutputStream, uri: Uri) {
        appContext.contentResolver.openInputStream(uri).use { input ->
            input!!.copyTo(stream)
        }
    }

    override fun update(file: File, action: (Bitmap) -> Bitmap) =
        save(file, action(loadBitmap(file)))

    private fun compressBitmap(bitmap: Bitmap, output: OutputStream) {
        // Jpeg format with "95" quality value is used - as same as ImageCapture settings
        // See [CameraManager.bindCameraUseCases]
        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, output)
    }

    private fun loadBitmap(file: File): Bitmap = BitmapFactory.decodeFile(file.absolutePath)
}