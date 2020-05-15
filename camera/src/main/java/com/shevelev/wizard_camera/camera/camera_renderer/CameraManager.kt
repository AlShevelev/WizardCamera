package com.shevelev.wizard_camera.camera.camera_renderer

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.view.Surface
import timber.log.Timber

class CameraManager(context: Context) {
    private companion object {
        const val DEBUG_TAG = "CAMERA_MANAGER"
    }

    private val appContext = context.applicationContext

    private val cameraService = appContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    private var cameraDevice: CameraDevice? = null
    private var cameraSession: CameraCaptureSession? = null

    /**
     * [openCameraResult] The argument it true in case of success
     */
    @SuppressLint("MissingPermission")
    fun openCamera(openCameraResult: (Boolean) -> Unit) {
        try {
            val cameraId = getBackCameraId()
            Timber.tag(DEBUG_TAG).d("Camera id is: $cameraId")
            if(cameraId == null) {
                openCameraResult(false)
                return
            }

            cameraService.openCamera(cameraId, object: CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    Timber.tag(DEBUG_TAG).d("Camera is opened")
                    cameraDevice = camera
                    openCameraResult(true)
                }

                override fun onDisconnected(camera: CameraDevice) {
                    Timber.tag(DEBUG_TAG).d("Camera is disconnected")

                    cameraDevice?.close()
                    cameraDevice = null
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    Timber.tag(DEBUG_TAG).d("Camera opening error. The code is: $error")
                    openCameraResult(false)
                }

            }, null)
        } catch (ex: Exception) {
            Timber.e(ex)
            openCameraResult(false)
        }
    }

    fun close() {
        cameraSession?.close()
        cameraDevice?.close()
    }

    /**
     * @return true in case of success
     */
    fun startPreview(surfaceTexture: SurfaceTexture, width: Int, height: Int, turnFlashOn: Boolean, callbackHandler: Handler): Boolean {
        val builder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        surfaceTexture.setDefaultBufferSize(height, width)
        val surface = Surface(surfaceTexture)
        builder.addTarget(surface)

        builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO)
        builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_START)

        if(turnFlashOn) {
            builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH)
        }

        var isSuccess = false
        val lock = Object()

        cameraDevice!!.createCaptureSession(listOf(surface), object: CameraCaptureSession.StateCallback() {
            override fun onConfigureFailed(session: CameraCaptureSession) {
                isSuccess = false
                session.close()
                synchronized(lock) {
                    lock.notify()
                }
            }

            override fun onConfigured(session: CameraCaptureSession) {
                isSuccess = true
                cameraSession = session
                session.setRepeatingRequest(builder.build(), null, null)
                synchronized(lock) {
                    lock.notify()
                }
            }

        }, callbackHandler)

        synchronized(lock) {
            lock.wait()
        }

        return isSuccess
    }

    private fun getBackCameraId(): String? {
        val cameraIds = cameraService.cameraIdList

        cameraIds.forEach { cameraId ->
            val cameraCharacteristics = cameraService.getCameraCharacteristics(cameraId)
            if(cameraCharacteristics[CameraCharacteristics.LENS_FACING] == CameraCharacteristics.LENS_FACING_BACK) {
                return cameraId
            }
        }

        return null
    }
}