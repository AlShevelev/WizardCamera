package com.shevelev.wizard_camera.core.camera_gl.api.bitmap.filters

import android.graphics.Bitmap
import android.opengl.GLSurfaceView
import android.os.Handler

/**
 * An interface for all OpenGL renderers
 */
interface GlSurfaceFilter : GLSurfaceView.Renderer {
    fun attachSurface(surface: GLSurfaceView)

    fun release()

    fun startGetFrameAsBitmap(handler: Handler, callback: (Bitmap?) -> Unit)
}