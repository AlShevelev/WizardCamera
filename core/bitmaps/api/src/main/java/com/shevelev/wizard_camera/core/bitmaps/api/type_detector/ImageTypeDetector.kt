package com.shevelev.wizard_camera.core.bitmaps.api.type_detector

import android.net.Uri
import java.io.InputStream

interface ImageTypeDetector {
    fun getImageType(imageUri: Uri): ImageType

    fun getImageType(inputStream: InputStream?): ImageType
}