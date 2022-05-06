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
    override fun save(bitmap: Bitmap, destination: File) {
        destination.outputStream().use {
            compressBitmap(bitmap, it)
        }
    }

    override fun save(bitmap: Bitmap, destination: Uri) {
        appContext.contentResolver.openOutputStream(destination).use {
            compressBitmap(bitmap, it!!)
        }
    }

    override fun copy(source: Uri, destination: OutputStream) {
        appContext.contentResolver.openInputStream(source).use { input ->
            input!!.copyTo(destination)
        }
    }

    override fun load(source: Uri): Bitmap =
        appContext.contentResolver.openInputStream(source).use {
            BitmapFactory.decodeStream(it)
        }

    override fun update(file: File, action: (Bitmap) -> Bitmap) = save(action(loadBitmap(file)), file)

    override fun update(uri: Uri, action: (Bitmap) -> Bitmap) = save(action(load(uri)), uri)

    private fun compressBitmap(bitmap: Bitmap, output: OutputStream) {
        // Jpeg format with "95" quality value is used - as same as ImageCapture settings
        // See [CameraManager.bindCameraUseCases]
        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, output)
    }

    private fun loadBitmap(file: File): Bitmap = BitmapFactory.decodeFile(file.absolutePath)
}