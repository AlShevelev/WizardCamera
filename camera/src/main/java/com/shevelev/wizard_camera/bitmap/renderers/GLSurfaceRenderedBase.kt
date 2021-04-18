package com.shevelev.wizard_camera.bitmap.renderers

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES31
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
        GLES31.glViewport(0, 0, width, height)
        GLES31.glClearColor(0f, 0f, 0f, 1f)

        surfaceSize = Size(width, height)

        createTextures()
        createProgram()
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // do nothing
    }

    abstract fun release()

    @CallSuper
    protected open fun createTextures() {
        // Create empty textures
        GLES31.glGenTextures(2, textures, 0)

        // Switch to the texture with index 0 and bind the photo to it
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textures[0])

        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_LINEAR)
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_LINEAR)
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_S, GLES31.GL_CLAMP_TO_EDGE)
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_T, GLES31.GL_CLAMP_TO_EDGE)

        GLUtils.texImage2D(GLES31.GL_TEXTURE_2D, 0, bitmap, 0)

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
        vertexShader = GLES31.glCreateShader(GLES31.GL_VERTEX_SHADER)
        GLES31.glShaderSource(vertexShader, context.getRawString(R.raw.vertext))
        GLES31.glCompileShader(vertexShader)

        // Init fragment shader code
        fragmentShader = GLES31.glCreateShader(GLES31.GL_FRAGMENT_SHADER)
        GLES31.glShaderSource(fragmentShader, context.getRawString(fragmentShaderResId))
        GLES31.glCompileShader(fragmentShader)

        // Init program
        program = GLES31.glCreateProgram()
        GLES31.glAttachShader(program, vertexShader)
        GLES31.glAttachShader(program, fragmentShader)

        GLES31.glLinkProgram(program)
    }

    protected fun draw(texture: Int) {
        setFragmentShaderParameters(texture)

        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)
        GLES31.glDrawArrays(GLES31.GL_TRIANGLE_STRIP, 0, 4)
    }

    @CallSuper
    protected open fun setFragmentShaderParameters(texture: Int) {
        GLES31.glBindFramebuffer(GLES31.GL_FRAMEBUFFER, 0)
        GLES31.glUseProgram(program)
        GLES31.glDisable(GLES31.GL_BLEND)

        val texturePositionHandle = GLES31.glGetAttribLocation(program, "vTexCoord")
        GLES31.glVertexAttribPointer(texturePositionHandle, 2, GLES31.GL_FLOAT, false, 0, textureBuffer)
        GLES31.glEnableVertexAttribArray(texturePositionHandle)

        val textureHandle = GLES31.glGetUniformLocation(program, "iChannel0")
        GLES31.glActiveTexture(GLES31.GL_TEXTURE0)
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, texture)
        GLES31.glUniform1i(textureHandle, 0)

        val positionHandle = GLES31.glGetAttribLocation(program, "vPosition")
        GLES31.glVertexAttribPointer(positionHandle, 2, GLES31.GL_FLOAT, false, 0, verticesBuffer)
        GLES31.glEnableVertexAttribArray(positionHandle)
    }
}