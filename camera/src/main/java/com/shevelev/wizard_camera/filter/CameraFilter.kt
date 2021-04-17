package com.shevelev.wizard_camera.filter

import android.content.Context
import android.opengl.GLES11Ext
import android.opengl.GLES31
import androidx.annotation.CallSuper
import androidx.annotation.RawRes
import com.shevelev.wizard_camera.camera.R
import com.shevelev.wizard_camera.camera.camera_renderer.RenderBuffer
import com.shevelev.wizard_camera.camera.camera_renderer.utils.ShaderUtils
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

open class CameraFilter(
    context: Context,
    @RawRes fragmentFilterResId: Int
) {
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
        private const val activeTextUnit = GLES31.GL_TEXTURE8

        private var cameraRenderBuffer: RenderBuffer? = null

        fun release() {
            mainProgram = 0
            cameraRenderBuffer = null
        }
    }

    private val startTime = System.currentTimeMillis()
    private var iFrame = 0

    protected val filterProgram: Int

    private lateinit var settings: FilterSettings

    init {
        if (mainProgram == 0) {
            mainProgram = ShaderUtils.buildProgram(context, R.raw.vertext, R.raw.original_rtt)
        }

        filterProgram = ShaderUtils.buildProgram(context, R.raw.vertext, fragmentFilterResId)
    }

    @CallSuper
    open fun onAttach(settings: FilterSettings) {
        iFrame = 0
        this.settings = settings
    }

    fun draw(cameraTexId: Int, canvasWidth: Int, canvasHeight: Int) {
        // Create texture for transformation
        if (cameraRenderBuffer == null || cameraRenderBuffer!!.width != canvasWidth || cameraRenderBuffer!!.height != canvasHeight) {
            cameraRenderBuffer = RenderBuffer(canvasWidth, canvasHeight, activeTextUnit)
        }

        // Use shaders for rendering texture from a camera
        GLES31.glUseProgram(mainProgram)

        // Prepare to render texture from a camera
        val iChannel0Location = GLES31.glGetUniformLocation(mainProgram, "iChannel0")
        GLES31.glActiveTexture(GLES31.GL_TEXTURE0)
        GLES31.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, cameraTexId)
        GLES31.glUniform1i(iChannel0Location, 0)

        val vPositionLocation = GLES31.glGetAttribLocation(mainProgram, "vPosition")
        GLES31.glEnableVertexAttribArray(vPositionLocation)
        GLES31.glVertexAttribPointer(vPositionLocation, 2, GLES31.GL_FLOAT, false, 4 * 2, vertexBuffer)

        val vTexCoordLocation = GLES31.glGetAttribLocation(mainProgram, "vTexCoord")
        GLES31.glEnableVertexAttribArray(vTexCoordLocation)
        GLES31.glVertexAttribPointer(vTexCoordLocation, 2, GLES31.GL_FLOAT, false, 4 * 2, rotatedTextureCoordinatesBuffer)

        // Render the texture from a camera to the texture in a buffer
        cameraRenderBuffer
            ?.let {
                it.bind()
                GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)
                GLES31.glDrawArrays(GLES31.GL_TRIANGLE_STRIP, 0, 4)
                it.unbind()
                GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)

                // Add transformation to texture from a camera
                onDraw(it.texId, canvasWidth, canvasHeight)
            }

        iFrame++
    }

    @Override
    protected open fun onDraw(cameraTexId: Int, canvasWidth: Int, canvasHeight: Int) {
        setupShaderInputs(filterProgram, intArrayOf(canvasWidth, canvasHeight), cameraTexId)
        GLES31.glDrawArrays(GLES31.GL_TRIANGLE_STRIP, 0, 4)
    }

    /**
     * [mainChannel] - set of textures id to render
     */
    open fun setupShaderInputs(program: Int, iResolution: IntArray, mainChannel: Int) {
        GLES31.glUseProgram(program)

        passSettingsParams(program, settings)

        // Screen size in pixels
        val iResolutionLocation = GLES31.glGetUniformLocation(program, "iResolution")
        GLES31.glUniform3fv(
            iResolutionLocation,
            1,
            FloatBuffer.wrap(floatArrayOf(iResolution[0].toFloat(), iResolution[1].toFloat(), 1.0f)))

        val time = (System.currentTimeMillis() - startTime) / 1000.0f
        val iGlobalTimeLocation = GLES31.glGetUniformLocation(program, "iGlobalTime")
        GLES31.glUniform1f(iGlobalTimeLocation, time)

        val iFrameLocation = GLES31.glGetUniformLocation(program, "iFrame")
        GLES31.glUniform1i(iFrameLocation, iFrame)

        val vPositionLocation = GLES31.glGetAttribLocation(program, "vPosition")
        GLES31.glEnableVertexAttribArray(vPositionLocation)
        GLES31.glVertexAttribPointer(vPositionLocation, 2, GLES31.GL_FLOAT, false, 4 * 2, vertexBuffer)

        val vTexCoordLocation = GLES31.glGetAttribLocation(program, "vTexCoord")
        GLES31.glEnableVertexAttribArray(vTexCoordLocation)
        GLES31.glVertexAttribPointer(vTexCoordLocation, 2, GLES31.GL_FLOAT, false, 4 * 2, textureCoordinatesBuffer)

        val sTextureLocation = GLES31.glGetUniformLocation(program, "iChannel0")
        GLES31.glActiveTexture(GLES31.GL_TEXTURE0)
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, mainChannel)
        GLES31.glUniform1i(sTextureLocation, 0)

        val iChannelResolutionLocation = GLES31.glGetUniformLocation(program, "iChannelResolution")
        GLES31.glUniform3fv(iChannelResolutionLocation, 0, FloatBuffer.wrap(FloatArray(0)))
    }

    /**
     * Pass filter-specific arguments
     */
    protected open fun passSettingsParams(program: Int, settings: FilterSettings) {
        // do nothing
    }
}
