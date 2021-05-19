package com.shevelev.wizard_camera.camera.renderer

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.EGL14
import android.opengl.GLES11Ext
import android.opengl.GLES31
import com.shevelev.wizard_camera.camera.filter.CameraFilter
import com.shevelev.wizard_camera.camera.renderer.utils.TextureUtils
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.shared.factory.FiltersFactory
import javax.microedition.khronos.egl.*

class GLRenderer(
    private var glWidth: Int,
    private var glHeight: Int,
    private val context: Context
) {
    private lateinit var egl10: EGL10
    private lateinit var eglDisplay: EGLDisplay
    private var eglSurface: EGLSurface? = null
    private lateinit var eglContext: EGLContext

    private lateinit var filter: CameraFilter

    lateinit var cameraSurfaceTexture: SurfaceTexture
        private set

    private var cameraTextureId: Int = 0

    fun initGL(texture: SurfaceTexture) {
        egl10 = EGLContext.getEGL() as EGL10

        eglDisplay = egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY)
        if (eglDisplay == EGL10.EGL_NO_DISPLAY) {
            throw Exception("eglGetDisplay failed: ${android.opengl.GLUtils.getEGLErrorString(egl10.eglGetError())}")
        }

        val version = IntArray(2)
        if (!egl10.eglInitialize(eglDisplay, version)) {
            throw Exception("eglInitialize failed: ${android.opengl.GLUtils.getEGLErrorString(egl10.eglGetError())}")
        }

        val configsCount = IntArray(1)
        val configs = arrayOfNulls<EGLConfig>(1)

        val configSpec = intArrayOf(
            EGL10.EGL_RENDERABLE_TYPE,
            EGL14.EGL_OPENGL_ES2_BIT,
            EGL10.EGL_RED_SIZE, 8,
            EGL10.EGL_GREEN_SIZE, 8,
            EGL10.EGL_BLUE_SIZE, 8,
            EGL10.EGL_ALPHA_SIZE, 8,
            EGL10.EGL_DEPTH_SIZE, 0,
            EGL10.EGL_STENCIL_SIZE, 0,
            EGL10.EGL_NONE
        )

        var eglConfig: EGLConfig? = null
        if (!egl10.eglChooseConfig(eglDisplay, configSpec, configs, 1, configsCount)) {
            throw IllegalArgumentException("eglChooseConfig failed: ${android.opengl.GLUtils.getEGLErrorString(egl10.eglGetError())}")
        } else if (configsCount[0] > 0) {
            eglConfig = configs[0]
        }
        if (eglConfig == null) {
            throw Exception("eglConfig not initialized")
        }

        val attrs = intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE)
        eglContext = egl10.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, attrs)
        eglSurface = egl10.eglCreateWindowSurface(eglDisplay, eglConfig, texture, null)

        if (eglSurface == null || eglSurface == EGL10.EGL_NO_SURFACE) {
            val error = egl10.eglGetError()
            throw Exception("eglCreateWindowSurface failed: ${android.opengl.GLUtils.getEGLErrorString(error)}")
        }

        if (!egl10.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
            throw RuntimeException("eglMakeCurrent failed: ${android.opengl.GLUtils.getEGLErrorString(egl10.eglGetError())}")
        }

        cameraTextureId = TextureUtils.createTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES)
        cameraSurfaceTexture = SurfaceTexture(cameraTextureId)

        cameraSurfaceTexture.setOnFrameAvailableListener {
            it.updateTexImage()
            renderFrame()
        }
    }

    fun setFilter(filterSettings: GlFilterSettings) {
        val selectedFilter = CameraFilter(context, FiltersFactory.getFilterRes(filterSettings.code))
        selectedFilter.onAttach(FiltersFactory.createGLFilterSettings(filterSettings, context))

        this.filter = selectedFilter
    }

    private fun renderFrame() {
        if (glWidth < 0 && glHeight < 0) {
            glWidth = -glWidth
            glHeight = -glHeight
            GLES31.glViewport(0, 0, glWidth, glHeight)
        }

        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)

        // Draw camera preview
        filter.draw(cameraTextureId, glWidth, glHeight)

        // Flush
        GLES31.glFlush()
        egl10.eglSwapBuffers(eglDisplay, eglSurface)
    }

    fun releaseGL() {
        cameraSurfaceTexture.setOnFrameAvailableListener(null)
        cameraSurfaceTexture.release()

        GLES31.glDeleteTextures(1, intArrayOf(cameraTextureId), 0)
    }
}