package com.shevelev.wizard_camera.core.bitmaps.impl.type_detector.signatures

import com.shevelev.wizard_camera.core.bitmaps.api.type_detector.ImageType

/**
 * Signatures factory
 * Byte sets for various images have been got from here:
 * http://jubin.tech/articles/2018/12/05/Detect-image-format-using-java.html
 */
internal object ImageSignatureFactory {
    fun getJpegSignature(): ImageSignature =
        ImageSignature(
            byteArrayOf(
                0xFF.toByte(),
                0xD8.toByte(),
                0xFF.toByte()
            ),
            ImageType.JPEG
        )

    fun getPngSignature(): ImageSignature =
        ImageSignature(
            byteArrayOf(
                0x89.toByte(),
                0x50.toByte(),      // P
                0x4E.toByte(),      // N
                0x47.toByte(),      // G
                0x0D.toByte(),
                0x0A.toByte(),
                0x1A.toByte(),
                0x0A.toByte()
            ),
            ImageType.PNG
        )
}