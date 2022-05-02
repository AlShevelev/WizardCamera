package com.shevelev.wizard_camera.core.bitmaps.api.bitmaps

import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.OutputStream

interface BitmapHelper {
    fun save(file: File, bitmap: Bitmap)

    fun save(file: File, uri: Uri)

    fun save(stream: OutputStream, uri: Uri)

    fun update(file: File, action: (Bitmap) -> Bitmap)
}