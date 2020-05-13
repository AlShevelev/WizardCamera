package com.shevelev.wizard_camera.camera.camera_renderer

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLES11Ext
import android.opengl.GLES31
import android.os.Handler
import android.view.TextureView
import android.widget.Toast
import com.shevelev.wizard_camera.camera.FiltersFactory
import com.shevelev.wizard_camera.camera.R
import com.shevelev.wizard_camera.camera.filter.CameraFilter
import com.shevelev.wizard_camera.camera.filter.FilterCode
import com.shevelev.wizard_camera.camera.utils.TextureUtils
import timber.log.Timber
import java.io.IOException
import javax.microedition.khronos.egl.*
import kotlin.math.absoluteValue

class CameraRenderer(private val context: Context) : Runnable, TextureView.SurfaceTextureListener {
    private companion object {
        const val EGL_OPENGL_ES2_BIT = 4
        const val EGL_CONTEXT_CLIENT_VERSION = 0x3098
        const val DRAW_INTERVAL: Long = 1000 / 30

        private var isFirstTimeStart = true
    }

    private var renderThread: Thread? = null
    private lateinit var surfaceTexture: SurfaceTexture
    private var glWidth = 0
    private var glHeight = 0

    private lateinit var eglDisplay: EGLDisplay
    private var eglSurface: EGLSurface? = null
    private lateinit var eglContext: EGLContext
    private lateinit var egl10: EGL10

    private var camera = CameraManager(context)

    private lateinit var cameraSurfaceTexture: SurfaceTexture
    private var cameraTextureId: Int = 0

    private lateinit var selectedFilter: CameraFilter
    private var selectedFilterId = FilterCode.ORIGINAL

    private lateinit var filtersFactory: FiltersFactory

    private val mainThreadHandler = Handler()

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        Timber.tag("CAMERA_RENDERER").d("onSurfaceTextureSizeChanged: $width $height")

        if(isFirstTimeStart) {
            startRendering(surface, width, height)
            isFirstTimeStart = false
        }
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {}

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        Timber.tag("CAMERA_RENDERER").d("onSurfaceTextureDestroyed")
        stopRendering()
        return true
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        Timber.tag("CAMERA_RENDERER").d("onSurfaceTextureAvailable: $width $height")

        if(!isFirstTimeStart) {
            startRendering(surface, width, height)
        }
    }

    override fun run() {
        initGL(surfaceTexture)

        filtersFactory = FiltersFactory(context)
        setSelectedFilter(selectedFilterId)

        // Create texture for camera preview
        cameraTextureId = TextureUtils.createTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES)
        cameraSurfaceTexture = SurfaceTexture(cameraTextureId)

        // Start camera preview
        val isSuccess = try {
            camera.startPreview(cameraSurfaceTexture, glWidth.absoluteValue, glHeight.absoluteValue, mainThreadHandler)
        } catch (ex: IOException) {
            Timber.e(ex)
            Toast.makeText(context, R.string.cameraError, Toast.LENGTH_LONG).show()
            false
        }

        if(isSuccess) {
            // Render loop
            while (!Thread.currentThread().isInterrupted) {
                try {
                    if (glWidth < 0 && glHeight < 0) {
                        glWidth = -glWidth
                        glHeight = -glHeight
                        GLES31.glViewport(0, 0, glWidth, glHeight)
                    }

                    GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)

                    // Update the camera preview texture by the very last frame
                    synchronized(this) {
                        cameraSurfaceTexture.updateTexImage()
                    }

                    // Draw camera preview
                    selectedFilter.draw(cameraTextureId, glWidth, glHeight)

                    // Flush
                    GLES31.glFlush()
                    egl10.eglSwapBuffers(eglDisplay, eglSurface)

                    Thread.sleep(DRAW_INTERVAL)

                } catch (ex: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
            }
        }

        cameraSurfaceTexture.release()
        GLES31.glDeleteTextures(1, intArrayOf(cameraTextureId), 0)
    }

    fun setSelectedFilter(code: FilterCode) {
        selectedFilterId = code
        selectedFilter = filtersFactory.getFilter(code)
        selectedFilter.onAttach()
    }

    private fun startRendering(surface: SurfaceTexture, width: Int, height: Int) {
        Timber.tag("CAMERA_RENDERER").d("Rendering started")

        renderThread
            ?.let {
                if(it.isAlive) {
                    it.interrupt()
                }
            }

        renderThread = Thread(this)

        surfaceTexture = surface
        glWidth = -width
        glHeight = -height

        // Open camera
        camera.openCamera { isSuccess ->
            if(isSuccess) {
                renderThread!!.start()          // Start rendering
            } else {
                Toast.makeText(context, R.string.cameraError, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun stopRendering() {
        Timber.tag("CAMERA_RENDERER").d("Rendering stopped")

        camera.close()

        renderThread
            ?.let {
                if(it.isAlive) {
                    it.interrupt()
                }
            }
        CameraFilter.release()
    }

    private fun initGL(texture: SurfaceTexture) {
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
            EGL_OPENGL_ES2_BIT,
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

        val attrs = intArrayOf(EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE)
        eglContext = egl10.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, attrs)
        eglSurface = egl10.eglCreateWindowSurface(eglDisplay, eglConfig, texture, null)

        if (eglSurface == null || eglSurface == EGL10.EGL_NO_SURFACE) {
            val error = egl10.eglGetError()
            throw Exception("eglCreateWindowSurface failed: ${android.opengl.GLUtils.getEGLErrorString(error)}")
        }

        if (!egl10.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
            throw RuntimeException("eglMakeCurrent failed: ${android.opengl.GLUtils.getEGLErrorString(egl10.eglGetError())}")
        }
    }
}