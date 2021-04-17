package com.shevelev.wizard_camera.bitmap.renderers

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLException
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.os.Handler
import android.util.Size
import androidx.annotation.CallSuper
import androidx.annotation.RawRes
import com.shevelev.my_footprints_remastered.utils.resources.getRawString
import com.shevelev.wizard_camera.camera.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Base renderer for all OpenGL renderers
 */
abstract class GLSurfaceRenderedBase(
    private val context: Context,
    private val bitmap: Bitmap,
    @RawRes
    private val fragmentShaderResId: Int
): GLSurfaceView.Renderer {

    private val vertices = floatArrayOf(
        -1f, -1f,           // Left-bottom
        1f, -1f,            // Right-bottom
        -1f, 1f,            // Left-top
        1f, 1f              // Right-top
    )

    private val textureVertices = floatArrayOf(
        0f, 1f,             // Left-bottom
        1f, 1f,             // Right-bottom
        0f, 0f,             // Left-top
        1f, 0f              // Right-top
    )

    private lateinit var verticesBuffer: FloatBuffer
    private lateinit var textureBuffer: FloatBuffer

    private var vertexShader: Int = 0
    private var fragmentShader: Int = 0
    protected var program: Int = 0

    protected val textures = IntArray(2)

    protected lateinit var surfaceSize: Size
    protected lateinit var surface: GLSurfaceView

    @get:Synchronized @set:Synchronized
    private var getFrameAsBitmapHandler: Handler? = null
    @get:Synchronized @set:Synchronized
    private var getFrameAsBitmapCallback: ((Bitmap?) -> Unit)? = null

    fun attachSurface(surface: GLSurfaceView) {
        this.surface = surface
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        GLES20.glClearColor(0f, 0f, 0f, 1f)

        surfaceSize = Size(width, height)

        createTextures()
        createProgram()
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // do nothing
    }

    fun startGetFrameAsBitmap(handler: Handler, callback: (Bitmap?) -> Unit) {
        getFrameAsBitmapHandler = handler
        getFrameAsBitmapCallback = callback
    }

    protected fun tryToGetFrameAsBitmap(gl: GL10) {
        getFrameAsBitmapHandler?.let { handler ->
            val bitmap = extractBitmapFromFrame(gl)

            handler.post {
                getFrameAsBitmapCallback?.invoke(bitmap)

                getFrameAsBitmapHandler = null
                getFrameAsBitmapCallback = null
            }
        }
    }

    @CallSuper
    protected open fun createTextures() {
        // Create empty textures
        GLES20.glGenTextures(2, textures, 0)

        // Switch to the texture with index 0 and bind the photo to it
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)

        // Init buffer with polygon vertexes
        var buffer = ByteBuffer.allocateDirect(vertices.size * 4)
        buffer.order(ByteOrder.nativeOrder())
        verticesBuffer = buffer.asFloatBuffer()
        verticesBuffer.put(vertices)
        verticesBuffer.position(0)

        // Init buffer with texture vertexes
        buffer = ByteBuffer.allocateDirect(textureVertices.size * 4)
        buffer.order(ByteOrder.nativeOrder())
        textureBuffer = buffer.asFloatBuffer()
        textureBuffer.put(textureVertices)
        textureBuffer.position(0)
    }

    private fun createProgram() {
        // Init vertex shader code
        vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER)
        GLES20.glShaderSource(vertexShader, context.getRawString(R.raw.vertext))
        GLES20.glCompileShader(vertexShader)

        // Init fragment shader code
        fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER)
        GLES20.glShaderSource(fragmentShader, context.getRawString(fragmentShaderResId))
        GLES20.glCompileShader(fragmentShader)

        // Init program
        program = GLES20.glCreateProgram()
        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)

        GLES20.glLinkProgram(program)
    }

    protected fun draw(texture: Int) {
        setFragmentShaderParameters(texture)

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }

    @CallSuper
    protected open fun setFragmentShaderParameters(texture: Int) {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        GLES20.glUseProgram(program)
        GLES20.glDisable(GLES20.GL_BLEND)

        val texturePositionHandle = GLES20.glGetAttribLocation(program, "vTexCoord")
        GLES20.glVertexAttribPointer(texturePositionHandle, 2, GLES20.GL_FLOAT, false, 0, textureBuffer)
        GLES20.glEnableVertexAttribArray(texturePositionHandle)

        val textureHandle = GLES20.glGetUniformLocation(program, "iChannel0")
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture)
        GLES20.glUniform1i(textureHandle, 0)

        val positionHandle = GLES20.glGetAttribLocation(program, "vPosition")
        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, verticesBuffer)
        GLES20.glEnableVertexAttribArray(positionHandle)
    }

    protected fun extractBitmapFromFrame(gl: GL10): Bitmap? {
        val x = 0
        val y = 0
        val width = surfaceSize.width
        val height = surfaceSize.height

        val bitmapBuffer = IntArray(width * height)
        val bitmapSource = IntArray(width * height)

        val intBuffer = IntBuffer.wrap(bitmapBuffer)
        intBuffer.position(0)

        try {
            gl.glReadPixels(x, y, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer)
            var offset1: Int
            var offset2: Int
            for (i in 0 until height) {
                offset1 = i * width
                offset2 = (height - i - 1) * width
                for (j in 0 until width) {
                    val texturePixel = bitmapBuffer[offset1 + j]
                    val blue = (texturePixel shr 16) and 0xff
                    val red = (texturePixel shl 16) and 0x00ff0000
                    val pixel = (texturePixel and 0xff00ff00.toInt()) or red or blue

                    bitmapSource[offset2 + j] = pixel
                }
            }
        } catch (ex: GLException) {
            ex.printStackTrace()
            return null
        }
        return Bitmap.createBitmap(bitmapSource, width, height, Bitmap.Config.ARGB_8888)
    }
}