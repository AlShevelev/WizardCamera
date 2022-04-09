package com.shevelev.wizard_camera.bitmap_images

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shevelev.wizard_camera.bitmap_images.image_type_detector.ImageType
import com.shevelev.wizard_camera.bitmap_images.image_type_detector.ImageTypeDetector
import com.shevelev.wizard_camera.bitmap_images.image_type_detector.ImageTypeDetectorImpl
import com.shevelev.wizard_camera.bitmap_images.image_type_detector.signatures.ImageSignatureFactory

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.BeforeClass

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ImageTypeDetectorTest {
    companion object {
        private lateinit var context: Context
        private lateinit var imageTypeDetector: ImageTypeDetector

        @BeforeClass
        @JvmStatic
        fun init() {
            context = InstrumentationRegistry.getInstrumentation().context

            imageTypeDetector = ImageTypeDetectorImpl(
                context,
                listOf(
                    ImageSignatureFactory.getJpegSignature(),
                    ImageSignatureFactory.getPngSignature()
                )
            )
        }
    }

    @Test
    fun checkJpeg() {
        // Arrange
        val stream = context.assets.open("sample_jpeg.jpg")

        // Act
        val imageType = imageTypeDetector.getImageType(stream)
        stream.close()

        // Assert
        assertEquals(ImageType.JPEG, imageType)
    }

    @Test
    fun checkPng() {
        // Arrange
        val stream = context.assets.open("sample_png.png")

        // Act
        val imageType = imageTypeDetector.getImageType(stream)
        stream.close()

        // Assert
        assertEquals(ImageType.PNG, imageType)
    }

    @Test
    fun checkUndefined() {
        // Arrange
        val stream = context.assets.open("sample_undefined.txt")

        // Act
        val imageType = imageTypeDetector.getImageType(stream)
        stream.close()

        // Assert
        assertEquals(ImageType.UNDEFINED, imageType)
    }

    @Test
    fun checkEmpty() {
        // Arrange
        val stream = context.assets.open("sample_empty.txt")

        // Act
        val imageType = imageTypeDetector.getImageType(stream)
        stream.close()

        // Assert
        assertEquals(ImageType.UNDEFINED, imageType)
    }

    @Test
    fun checkNull() {
        // Arrange
        val stream = null

        // Act
        val imageType = imageTypeDetector.getImageType(stream)

        // Assert
        assertEquals(ImageType.UNDEFINED, imageType)
    }
}