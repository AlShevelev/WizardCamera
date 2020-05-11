package com.shevelev.wizard_camera.camera.filter

import android.content.Context
import android.opengl.GLES20
import com.shevelev.wizard_camera.camera.R
import com.shevelev.wizard_camera.camera.utils.TextureUtils

class RefractionCameraFilter(context: Context) : CameraFilter(context, R.raw.refraction) {
    // Load the texture will need for the shader
    private val texture2Id = TextureUtils.loadTexture(context, R.raw.tex11, IntArray(2))

    public override fun onDraw(cameraTexId: Int, canvasWidth: Int, canvasHeight: Int) {
        setupShaderInputs(filterProgram, intArrayOf(canvasWidth, canvasHeight), intArrayOf(cameraTexId, texture2Id))
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }
}

