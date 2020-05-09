package com.shevelev.wizard_camera.gl.filter

import android.content.Context
import android.opengl.GLES11Ext
import android.opengl.GLES20
import androidx.annotation.CallSuper
import androidx.annotation.RawRes
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.gl.RenderBuffer
import com.shevelev.wizard_camera.gl.utils.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

open class CameraFilter(context: Context, @RawRes fragmentFilterResId: Int) {
    companion object {
        private val squareCoordinates = floatArrayOf(
            1.0f, -1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            -1.0f, 1.0f
        )

        private val textureCoordinates = floatArrayOf(
            1.0f, 0.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f
        )

        private val rotatedTextureCoordinates = floatArrayOf(
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            0.0f, 1.0f
        )

        private val vertexBuffer =
            ByteBuffer.allocateDirect(squareCoordinates.size * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .apply {
                    put(squareCoordinates)
                    position(0)
                }


        private val textureCoordinatesBuffer =
            ByteBuffer.allocateDirect(textureCoordinates.size * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .apply {
                    put(textureCoordinates)
                    position(0)
                }

        private val rotatedTextureCoordinatesBuffer =
            ByteBuffer.allocateDirect(rotatedTextureCoordinates.size * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .apply {
                    put(rotatedTextureCoordinates)
                    position(0)
                }

        private var mainProgram = 0
        private const val activeTextUnit = GLES20.GL_TEXTURE8

        private var cameraRenderBuffer: RenderBuffer? = null

        fun release() {
            mainProgram = 0
            cameraRenderBuffer = null
        }
    }

    private val startTime = System.currentTimeMillis()
    private var iFrame = 0

    protected val filterProgram: Int

    init {
        if (mainProgram == 0) {
            mainProgram = ShaderUtils.buildProgram(context, R.raw.vertext, R.raw.original_rtt)
        }

        filterProgram = ShaderUtils.buildProgram(context, R.raw.vertext, fragmentFilterResId)
    }


    @CallSuper
    fun onAttach() {
        iFrame = 0
    }

    fun draw(cameraTexId: Int, canvasWidth: Int, canvasHeight: Int) {
        // Create texture for transformation
        if (cameraRenderBuffer == null || cameraRenderBuffer!!.width != canvasWidth || cameraRenderBuffer!!.height != canvasHeight) {
            cameraRenderBuffer = RenderBuffer(canvasWidth, canvasHeight, activeTextUnit)
        }

        // Use shaders for rendering texture from a camera
        GLES20.glUseProgram(mainProgram)

        // Prepare to render texture from a camera
        val iChannel0Location = GLES20.glGetUniformLocation(mainProgram, "iChannel0")
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, cameraTexId)
        GLES20.glUniform1i(iChannel0Location, 0)

        val vPositionLocation = GLES20.glGetAttribLocation(mainProgram, "vPosition")
        GLES20.glEnableVertexAttribArray(vPositionLocation)
        GLES20.glVertexAttribPointer(vPositionLocation, 2, GLES20.GL_FLOAT, false, 4 * 2, vertexBuffer)

        val vTexCoordLocation = GLES20.glGetAttribLocation(mainProgram, "vTexCoord")
        GLES20.glEnableVertexAttribArray(vTexCoordLocation)
        GLES20.glVertexAttribPointer(vTexCoordLocation, 2, GLES20.GL_FLOAT, false, 4 * 2, rotatedTextureCoordinatesBuffer)

        // Render the texture from a camera to the texture in a buffer
        cameraRenderBuffer
            ?.let {
                it.bind()
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
                it.unbind()
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

                // Add transformation to texture from a camera
                onDraw(it.texId, canvasWidth, canvasHeight)
            }

        iFrame++
    }

    @Override
    protected open fun onDraw(cameraTexId: Int, canvasWidth: Int, canvasHeight: Int) {
        setupShaderInputs(filterProgram, intArrayOf(canvasWidth, canvasHeight), intArrayOf(cameraTexId))
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }

    /**
     * [iChannels] - set of textures id to render
     */
    fun setupShaderInputs(program: Int, iResolution: IntArray, iChannels: IntArray) {
        GLES20.glUseProgram(program)

        val iResolutionLocation = GLES20.glGetUniformLocation(program, "iResolution")
        GLES20.glUniform3fv(
            iResolutionLocation,
            1,
            FloatBuffer.wrap(floatArrayOf(iResolution[0].toFloat(), iResolution[1].toFloat(), 1.0f)))

        val time = (System.currentTimeMillis() - startTime) / 1000.0f
        val iGlobalTimeLocation = GLES20.glGetUniformLocation(program, "iGlobalTime")
        GLES20.glUniform1f(iGlobalTimeLocation, time)

        val iFrameLocation = GLES20.glGetUniformLocation(program, "iFrame")
        GLES20.glUniform1i(iFrameLocation, iFrame)

        val vPositionLocation = GLES20.glGetAttribLocation(program, "vPosition")
        GLES20.glEnableVertexAttribArray(vPositionLocation)
        GLES20.glVertexAttribPointer(vPositionLocation, 2, GLES20.GL_FLOAT, false, 4 * 2, vertexBuffer)

        val vTexCoordLocation = GLES20.glGetAttribLocation(program, "vTexCoord")
        GLES20.glEnableVertexAttribArray(vTexCoordLocation)
        GLES20.glVertexAttribPointer(vTexCoordLocation, 2, GLES20.GL_FLOAT, false, 4 * 2, textureCoordinatesBuffer)

        iChannels.forEachIndexed { i, _ ->
            val sTextureLocation = GLES20.glGetUniformLocation(program, "iChannel$i")
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + i)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, iChannels[i])
            GLES20.glUniform1i(sTextureLocation, i)
        }

        val iChannelResolutionLocation = GLES20.glGetUniformLocation(program, "iChannelResolution")
        GLES20.glUniform3fv(iChannelResolutionLocation, 0, FloatBuffer.wrap(FloatArray(0)))
    }
}
