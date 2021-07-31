package com.shevelev.wizard_camera.shared.bitmap

import android.graphics.Bitmap
import java.io.File

interface BitmapHelper {
    fun saveBitmap(file: File, bitmap: Bitmap)
}