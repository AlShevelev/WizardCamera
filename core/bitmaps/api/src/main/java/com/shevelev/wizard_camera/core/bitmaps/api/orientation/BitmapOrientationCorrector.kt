package com.shevelev.wizard_camera.core.bitmaps.api.orientation

import android.graphics.Bitmap
import java.io.File
import java.io.InputStream

interface BitmapOrientationCorrector {
    fun getOrientation(file: File): Orientation?

    fun getOrientation(stream: InputStream): Orientation?

    fun rotate(bitmap: Bitmap, orientation: Orientation): Bitmap
}