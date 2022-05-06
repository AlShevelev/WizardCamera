package com.shevelev.wizard_camera.core.bitmaps.api.orientation

import android.graphics.Bitmap
import android.net.Uri
import java.io.File

interface BitmapOrientationCorrector {
    fun getOrientation(file: File): Orientation?

    fun getOrientation(uri: Uri): Orientation?

    fun rotate(bitmap: Bitmap, orientation: Orientation): Bitmap
}