package com.shevelev.wizard_camera.camera.manager

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.Range
import android.view.TextureView
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.FLASH_MODE_OFF
import androidx.camera.core.ImageCapture.FLASH_MODE_ON
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.shevelev.wizard_camera.camera.settings_repository.CameraSettingsRepository
import com.shevelev.wizard_camera.utils.useful_ext.fitInRange
import com.shevelev.wizard_camera.utils.useful_ext.reduceToRange
import timber.log.Timber
import java.io.File
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class CameraManager(private val cameraSettingsRepository: CameraSettingsRepository) {
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null

    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null

    val isFlashLightSupported: Boolean
        get() = camera?.cameraInfo?.hasFlashUnit() ?: false

    /** Blocking camera operations are performed using this executor */
    private var cameraExecutor: ExecutorService? = null

    fun initCamera(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        surface: SurfaceTexture,
        screenTexture: TextureView,
        cameraInitCompleted: () -> Unit) {

        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({

            // CameraProvider
            cameraProvider = cameraProviderFuture.get()

            // Build and bind the camera use cases
            bindCameraUseCases(lifecycleOwner, surface, screenTexture, CameraSelector.LENS_FACING_BACK)

            cameraInitCompleted()
        }, ContextCompat.getMainExecutor(context))
    }

    fun releaseCamera() {
        // Shut down our background executor
        cameraExecutor?.shutdown()
        cameraExecutor = null
    }

    /**
     * Zooms a camera preview
     * @param scaleFactor a scale factor value
     * @return a zoom ration factor
     */
    fun zoom(scaleFactor: Float): Float? =
        camera?.let { camera ->
            camera.cameraInfo.zoomState.value?.let { zoomInfo ->
                val minRatio = zoomInfo.minZoomRatio
                val maxRatio = zoomInfo.maxZoomRatio

                val ratio = (zoomInfo.zoomRatio * scaleFactor).fitInRange(Range(minRatio, maxRatio))
                camera.cameraControl.setZoomRatio(ratio)

                ratio
            }
        }

    fun updateExposure(exposureFactor: Float) {
        camera?.let { camera ->
            camera.cameraInfo.exposureState.let { exposureInfo ->
                val sourceRange = exposureInfo.exposureCompensationRange
                val calculationRange = Range(sourceRange.lower.toFloat(), sourceRange.upper.toFloat())
                val calculatedFactor = exposureFactor.reduceToRange(Range(-1f, 1f), calculationRange)

                camera.cameraControl.setExposureCompensationIndex(calculatedFactor.toInt())
            }
        }
    }

    /**
     * Captures an image
     * @param imageFile a file in which an image will be saved
     * @param useFlashLight if the value is "true" a flash light will be used, otherwise not
     * @param saveCompleted a callback which is called when a saving is completed (a value "true" is passed in case of success)
     */
    fun capture(imageFile: File, useFlashLight: Boolean, rotation: Int, saveCompleted: (Boolean) -> Unit) {
        imageCapture?.let { imageCapture ->
            imageCapture.targetRotation = rotation

            val metadata = ImageCapture.Metadata().apply {
                isReversedHorizontal = false
            }

            val outputOptions = ImageCapture.OutputFileOptions.Builder(imageFile)
                .setMetadata(metadata)
                .build()

            imageCapture.flashMode = if(useFlashLight) FLASH_MODE_ON else FLASH_MODE_OFF

            imageCapture.takePicture(outputOptions, cameraExecutor!!, object : ImageCapture.OnImageSavedCallback {
                override fun onError(ex: ImageCaptureException) {
                    Timber.e(ex)
                    saveCompleted(false)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    saveCompleted(true)
                }
            })
        }
    }

    /** Declare and bind preview, capture and analysis use cases */
    private fun bindCameraUseCases(
        lifecycleOwner: LifecycleOwner,
        cameraSurface: SurfaceTexture,
        screenTexture: TextureView,
        lensFacing: Int) {

        // Get screen metrics used to setup camera for full screen resolution

        val screenAspectRatio = aspectRatio(screenTexture.width, screenTexture.height)

        val rotation = screenTexture.display.rotation

        // CameraProvider
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")

        // CameraSelector
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        // Preview
        preview = Preview.Builder()
            // We request aspect ratio but no resolution
            .setTargetAspectRatio(screenAspectRatio)
            // Set initial target rotation
            .setTargetRotation(rotation)
            //          .setTargetResolution(Size(textureView.width, textureView.height))
            .build()

        // ImageCapture
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            // We request aspect ratio but no resolution to match preview config, but letting
            // CameraX optimize for whatever specific resolution best fits our use cases
            .setTargetAspectRatio(screenAspectRatio)
            // Set initial target rotation, we will have to call this again if rotation changes
            // during the lifecycle of this use case
            .setTargetRotation(rotation)
            .build()

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            camera = cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)

            // Attach the viewfinder's surface provider to preview use case
            //preview?.setSurfaceProvider(viewFinder.surfaceProvider)

            preview?.setSurfaceProvider(PreviewSurfaceProvider(
                cameraSurface,
                cameraSettingsRepository.optimalOutputSize,
                cameraExecutor!!))

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /**
     *  [androidx.camera.core.ImageAnalysisConfig] requires enum value of
     *  [androidx.camera.core.AspectRatio]. Currently it has values of 4:3 & 16:9.
     *
     *  Detecting the most suitable ratio for dimensions provided in @params by counting absolute
     *  of preview ratio to one of the provided values.
     *
     *  @param width - preview width
     *  @param height - preview height
     *  @return suitable aspect ratio
     */
    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }

        return AspectRatio.RATIO_16_9
    }

    companion object {
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }
}