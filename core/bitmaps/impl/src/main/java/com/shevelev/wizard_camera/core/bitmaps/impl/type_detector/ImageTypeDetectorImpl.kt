package com.shevelev.wizard_camera.core.bitmaps.impl.type_detector

import android.content.Context
import android.net.Uri
import com.shevelev.wizard_camera.core.bitmaps.api.type_detector.ImageType
import com.shevelev.wizard_camera.core.bitmaps.api.type_detector.ImageTypeDetector
import com.shevelev.wizard_camera.core.bitmaps.impl.type_detector.signatures.ImageSignature
import java.io.InputStream

internal class ImageTypeDetectorImpl(
    private val appContext: Context,
    private val signatures: List<ImageSignature>,
) : ImageTypeDetector {

    override fun getImageType(imageUri: Uri): ImageType {
        appContext.contentResolver.openInputStream(imageUri).use { inputStream ->
            if(inputStream != null) {
                return getImageType(inputStream)
            }
        }

        return ImageType.UNDEFINED
    }

    override fun getImageType(inputStream: InputStream?): ImageType {
        val buffer = ByteArray(signatures.maxOf { it.size })
        var isRead = false

        if(inputStream != null) {
            inputStream.read(buffer)
            isRead = true
        }

        if(!isRead) {
            return ImageType.UNDEFINED
        }

        signatures.forEach {
            if(it.check(buffer)) {
                return it.type
            }
        }

        return ImageType.UNDEFINED
    }
}