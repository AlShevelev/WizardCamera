package com.shevelev.wizard_camera.core.camera_gl.api.camera.manager

import android.content.Context
import android.graphics.SurfaceTexture
import android.view.TextureView
import androidx.lifecycle.LifecycleOwner
import java.io.File

interface CameraManager {
    val isFlashLightSupported: Boolean

    fun initCamera(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        surface: SurfaceTexture,
        screenTexture: TextureView,
        cameraInitCompleted: () -> Unit)

    fun releaseCamera()

    /**
     * Zooms a camera preview
     * @param scaleFactor a scale factor value
     * @return a zoom ration factor
     */
    fun zoom(scaleFactor: Float): Float?

    fun updateExposure(exposureFactor: Float)

    /**
     * Captures an image
     * @param imageFile a file in which an image will be saved
     * @param useFlashLight if the value is "true" a flash light will be used, otherwise not
     * @param saveCompleted a callback which is called when a saving is completed (a value "true" is passed in case of success)
     */
    fun capture(imageFile: File, useFlashLight: Boolean, rotation: Int, saveCompleted: (Boolean) -> Unit)
}