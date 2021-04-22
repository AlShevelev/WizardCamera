package com.shevelev.wizard_camera.bitmap.filters.fragment

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES31
import android.util.Size
import com.shevelev.wizard_camera.bitmap.filters.GLSurfaceShaderFilterBase
import com.shevelev.wizard_camera.camera.R
import com.shevelev.wizard_camera.camera.camera_renderer.utils.BufferUtils

class NewspaperSurfaceFilter(
    context: Context,
    bitmap: Bitmap,
    private var isGrayscale: Boolean,
    screenSize: Size
) : GLSurfaceShaderFilterBase(context, bitmap, R.raw.newspaper) {

    private val resolutionBuffer = BufferUtils.createBuffer(screenSize.width.toFloat(), screenSize.height.toFloat())

    override fun setFragmentShaderParameters(texture: Int) {
        super.setFragmentShaderParameters(texture)

        // Screen size in pixels
        val iResolutionHandle = GLES31.glGetUniformLocation(program, "iResolution")
        GLES31.glUniform3fv(iResolutionHandle, 1, resolutionBuffer)

        val grayscale = GLES31.glGetUniformLocation(program, "grayscale")
        GLES31.glUniform1i(grayscale, if(isGrayscale) 1 else 0)
    }

    fun updateGrayscale(isGrayscale: Boolean) {
        this.isGrayscale = isGrayscale
        surface.requestRender()
    }
}