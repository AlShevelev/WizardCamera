package com.shevelev.wizard_camera.gl.filter

import android.content.Context
import android.opengl.GLES20
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.gl.utils.TextureUtils

class MappingCameraFilter(context: Context) : CameraFilter(context, R.raw.mapping) {
    // Load the texture will need for the shader
    private val texture2Id = TextureUtils.loadTexture(context, R.raw.tex00, IntArray(2))

    public override fun onDraw(cameraTexId: Int, canvasWidth: Int, canvasHeight: Int) {
        setupShaderInputs(filterProgram, intArrayOf(canvasWidth, canvasHeight), intArrayOf(cameraTexId, texture2Id))
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }
}