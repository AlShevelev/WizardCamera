package com.shevelev.wizard_camera.bitmap_images.image_type_detector.signatures

import com.shevelev.wizard_camera.bitmap_images.image_type_detector.ImageType

/**
 * Signatures factory
 * Byte sets for various images have been got from here:
 * http://jubin.tech/articles/2018/12/05/Detect-image-format-using-java.html
 */
object ImageSignatureFactory {
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
                'P'.toByte(),
                'N'.toByte(),
                'G'.toByte(),
                0x0D.toByte(),
                0x0A.toByte(),
                0x1A.toByte(),
                0x0A.toByte()
            ),
            ImageType.PNG
        )
}