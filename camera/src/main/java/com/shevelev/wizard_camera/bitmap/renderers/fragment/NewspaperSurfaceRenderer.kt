package com.shevelev.wizard_camera.bitmap.renderers.fragment

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLES31
import com.shevelev.wizard_camera.bitmap.renderers.GLSurfaceShaderRenderedBase
import com.shevelev.wizard_camera.camera.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class NewspaperSurfaceRenderer(
    context: Context,
    bitmap: Bitmap,
    private var isGrayscale: Boolean
) : GLSurfaceShaderRenderedBase(context, bitmap, R.raw.newspaper) {

    private val bitmapSize = floatArrayOf(bitmap.width.toFloat(), bitmap.height.toFloat(), 1f)

    private lateinit var resolutionBuffer: FloatBuffer

    override fun createTextures() {
        super.createTextures()

        val buffer = ByteBuffer.allocateDirect(bitmapSize.size * 4)
        buffer.order(ByteOrder.nativeOrder())
        resolutionBuffer = buffer.asFloatBuffer()
        resolutionBuffer.put(bitmapSize)
        resolutionBuffer.position(0)
    }

    override fun setFragmentShaderParameters(texture: Int) {
        super.setFragmentShaderParameters(texture)

        // Screen size in pixels
        val iResolutionHandle = GLES20.glGetUniformLocation(program, "iResolution")
        GLES20.glUniform3fv(iResolutionHandle, 1, resolutionBuffer)

        val grayscale = GLES31.glGetUniformLocation(program, "grayscale")
        GLES31.glUniform1i(grayscale, if(isGrayscale) 1 else 0)
    }

    fun updateGrayscale(isGrayscale: Boolean) {
        this.isGrayscale = isGrayscale
        surface.requestRender()
    }
}