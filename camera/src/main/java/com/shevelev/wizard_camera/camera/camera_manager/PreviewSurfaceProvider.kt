package com.shevelev.wizard_camera.camera.camera_manager

import android.graphics.SurfaceTexture
import android.util.Size
import android.view.Surface
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import java.util.concurrent.Executor

class PreviewSurfaceProvider (
    private val surfaceTexture: SurfaceTexture,
    private val optimalOutputSize: Size,
    private val executor: Executor
): Preview.SurfaceProvider {
    override fun onSurfaceRequested(request: SurfaceRequest) {
        surfaceTexture.setDefaultBufferSize(optimalOutputSize.width, optimalOutputSize.height)
        request.provideSurface(Surface(surfaceTexture), executor, {
            // do noting
        })
    }
}