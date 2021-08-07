package com.shevelev.wizard_camera.bitmap_images.bitmap_utils

import android.graphics.Bitmap
import android.net.Uri
import java.io.File

interface BitmapHelper {
    fun saveBitmap(file: File, bitmap: Bitmap)

    fun saveBitmap(file: File, uri: Uri)

    fun checkAndCorrectOrientation(file: File)
}