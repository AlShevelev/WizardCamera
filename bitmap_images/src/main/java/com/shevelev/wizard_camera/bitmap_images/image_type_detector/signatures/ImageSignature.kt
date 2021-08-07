package com.shevelev.wizard_camera.bitmap_images.image_type_detector.signatures

import com.shevelev.wizard_camera.bitmap_images.image_type_detector.ImageType

class ImageSignature(private val signature: ByteArray, val type: ImageType) {
    /**
     * Size of signature in bytes
     */
    val size: Int
        get() = signature.size

    /**
     * Checks that the array matches the signature
     * @param checkedArray array to check
     */
    fun check(checkedArray: ByteArray): Boolean {
        if(size > checkedArray.size) {
            return false
        }

        for(i in signature.indices) {
            if(signature[i] != checkedArray[i]) {
                return false
            }
        }

        return true
    }
}