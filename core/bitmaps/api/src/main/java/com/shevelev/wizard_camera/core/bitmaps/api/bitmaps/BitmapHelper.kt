package com.shevelev.wizard_camera.core.bitmaps.api.bitmaps

import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.OutputStream

interface BitmapHelper {
    fun save(bitmap: Bitmap, destination: File)

    fun save(bitmap: Bitmap, destination: Uri)

    fun copy(source: Uri, destination: File)

    fun copy(source: Uri, destination: OutputStream)

    fun load(source: Uri): Bitmap

    fun update(file: File, action: (Bitmap) -> Bitmap)
}