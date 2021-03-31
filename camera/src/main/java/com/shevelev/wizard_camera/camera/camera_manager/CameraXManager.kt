package com.shevelev.wizard_camera.camera.camera_manager

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.Range
import android.view.TextureView
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.shevelev.wizard_camera.camera.camera_settings_repository.CameraSettingsRepository
import com.shevelev.wizard_camera.utils.useful_ext.fitInRange
import com.shevelev.wizard_camera.utils.useful_ext.reduceToRange
import timber.log.Timber
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class CameraXManager(private val cameraSettingsRepository: CameraSettingsRepository) {
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null

    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null


    /** Blocking camera operations are performed using this executor */
    private var cameraExecutor: ExecutorService? = null

    fun initCamera(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        surface: SurfaceTexture,
        screenTexture: TextureView,
        lensFacing: Int) {

        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({

            // CameraProvider
            cameraProvider = cameraProviderFuture.get()

            // Build and bind the camera use cases
            bindCameraUseCases(context, lifecycleOwner, surface, screenTexture, lensFacing)
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
            camera.cameraInfo.exposureState?.let { exposureInfo ->
                val sourceRange = exposureInfo.exposureCompensationRange
                val calculationRange = Range(sourceRange.lower.toFloat(), sourceRange.upper.toFloat())
                val calculatedFactor = exposureFactor.reduceToRange(Range(-1f, 1f), calculationRange)

                camera.cameraControl.setExposureCompensationIndex(calculatedFactor.toInt())
            }
        }
    }

    fun capture(context: Context) {
/*
        // Get a stable reference of the modifiable image capture use case
        imageCapture?.let { imageCapture ->

            val outputDirectory = context.MainActivity.getOutputDirectory(context)

            // Create output file to hold the image
            val photoFile = File(outputDirectory, "1.jpg")

            // Setup image capture metadata
            val metadata = ImageCapture.Metadata().apply {
                isReversedHorizontal = false
            }

            // Create output options object which contains file + metadata
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
                .setMetadata(metadata)
                .build()

            // Setup image capture listener which is triggered after photo has been taken
            imageCapture.takePicture(
                outputOptions, cameraExecutor!!, object : ImageCapture.OnImageSavedCallback {
                override fun onError(ex: ImageCaptureException) {
                    ex.printStackTrace()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: Uri.fromFile(photoFile)

                    // If the folder selected is an external media directory, this is
                    // unnecessary but otherwise other apps will not be able to access our
                    // images unless we scan them using [MediaScannerConnection]
                    val mimeType = MimeTypeMap.getSingleton()
                        .getMimeTypeFromExtension(savedUri.toFile().extension)
                    MediaScannerConnection.scanFile(context, arrayOf(savedUri.toFile().absolutePath), arrayOf(mimeType)) { _, _ -> }
                }
            })
        }
*/
    }

    /** Declare and bind preview, capture and analysis use cases */
    private fun bindCameraUseCases(
        context: Context,
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