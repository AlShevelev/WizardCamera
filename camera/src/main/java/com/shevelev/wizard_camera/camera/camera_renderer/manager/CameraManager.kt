package com.shevelev.wizard_camera.camera.camera_renderer.manager

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.CameraManager
import android.hardware.camera2.params.MeteringRectangle
import android.os.Handler
import android.util.Range
import android.util.Size
import android.util.SizeF
import android.view.Surface
import com.shevelev.wizard_camera.utils.useful_ext.fitInRange
import com.shevelev.wizard_camera.utils.useful_ext.ifNotNull
import com.shevelev.wizard_camera.utils.useful_ext.reduceToRange
import timber.log.Timber

class CameraManager(context: Context) {
    private companion object {
        const val DEBUG_TAG = "CAMERA_MANAGER"
    }

    private val appContext = context.applicationContext

    private val cameraService = appContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    private var cameraDevice: CameraDevice? = null
    private var cameraSession: CameraCaptureSession? = null

    private var requestBuilder: CaptureRequest.Builder? = null

    private var cameraInfo: CameraInfo? = null

    private var manualFocusEngaged = false

    private var priorZoomTouchDistance = 0f
    private val zoomSensitivity = 0.65f
    private val  minZoom = 1f
    private var zoomLevel = minZoom

    /**
     * [openCameraResult] The argument it true in case of success
     */
    @SuppressLint("MissingPermission")
    fun openCamera(openCameraResult: (Boolean) -> Unit) {
        try {
            cameraInfo = getBackCameraInfo()
            if(cameraInfo == null) {
                openCameraResult(false)
                return
            }

            cameraService.openCamera(cameraInfo!!.id, object: CameraDevice.StateCallback() {
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

        cameraSession = null
        cameraDevice = null
        requestBuilder = null
        cameraInfo = null

        priorZoomTouchDistance = 0f
        zoomLevel = minZoom
    }

    /**
     * @return true in case of success
     */
    fun startPreview(surfaceTexture: SurfaceTexture, viewSize: Size, settings: CameraSettings, callbackHandler: Handler): Boolean {
        val surface = Surface(surfaceTexture)

        requestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).also { builder ->
            surfaceTexture.setDefaultBufferSize(viewSize.height, viewSize.width)
            builder.addTarget(surface)

            // Automatic continuous focus
            if(settings.isAutoFocus) {
                builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
                builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_IDLE)
                builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
            }

            if(settings.turnFlashOn) {
                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH)
            }
        }

        var isSuccess = false
        val lock = Object()

        cameraDevice!!.createCaptureSession(listOf(surface), object: CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                isSuccess = true
                cameraSession = session
                session.setRepeatingRequest(requestBuilder!!.build(), null, null)
                synchronized(lock) {
                    lock.notify()
                }
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                isSuccess = false
                session.close()
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

    fun updateFlashState(turnFlashOn: Boolean) {
        ifNotNull(cameraSession, requestBuilder) { cameraSession, requestBuilder ->
            cameraSession.stopRepeating()
            requestBuilder.set(CaptureRequest.FLASH_MODE, if (turnFlashOn) CaptureRequest.FLASH_MODE_TORCH else null)
            cameraSession.setRepeatingRequest(requestBuilder.build(), null, null)

        }
    }

    fun setAutoFocus() {
        ifNotNull(cameraSession, requestBuilder) { cameraSession, requestBuilder ->
            cameraSession.stopRepeating()

            requestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL)
            requestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_OFF)
            requestBuilder.setTag("AUTO_FOCUS_TAG")

            cameraSession.capture(requestBuilder.build(), object: CameraCaptureSession.CaptureCallback() {
                override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
                    super.onCaptureCompleted(session, request, result)

                    if (request.tag == "AUTO_FOCUS_TAG") {
                        requestBuilder.set(CaptureRequest.CONTROL_AF_REGIONS, null)
                        requestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
                        requestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_IDLE)
                        requestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)

                        cameraSession.setRepeatingRequest(requestBuilder.build(), null, null)
                    }
                }

                override fun onCaptureFailed(session: CameraCaptureSession, request: CaptureRequest, failure: CaptureFailure) {
                    super.onCaptureFailed(session, request, failure)
                    Timber.e("Capture failed: ${failure.reason}")
                }

            }, null)
        }
    }

    /**
     * Switch to manual focus mode
     */
    fun setManualFocus(touchPoint: PointF, touchAreaSize: SizeF) {
        if(manualFocusEngaged) {
            return      // Focusing in progress
        }

        // Calculate focus area
        val y = ((touchPoint.x / touchAreaSize.width)  * cameraInfo!!.sensorArraySize.height().toFloat()).toInt()
        val x = ((touchPoint.y / touchAreaSize.height)  * cameraInfo!!.sensorArraySize.width().toFloat()).toInt()
        val halfTouchWidth  = 150
        val halfTouchHeight = 150

        val focusAreaTouch = MeteringRectangle(
            (x - halfTouchWidth).coerceAtLeast(0),
            (y - halfTouchHeight).coerceAtLeast(0),
            halfTouchWidth  * 2,
            halfTouchHeight * 2,
            MeteringRectangle.METERING_WEIGHT_MAX - 1)

        val callback = object : CameraCaptureSession.CaptureCallback() {
            override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
                super.onCaptureCompleted(session, request, result)

                manualFocusEngaged = false

                if (request.tag == "FOCUS_TAG") {
                    requestBuilder!!.set(CaptureRequest.CONTROL_AF_TRIGGER, null)
                    cameraSession!!.setRepeatingRequest(requestBuilder!!.build(), null, null)
                }
            }

            override fun onCaptureFailed(session: CameraCaptureSession, request: CaptureRequest, failure: CaptureFailure) {
                super.onCaptureFailed(session, request, failure)
                manualFocusEngaged = false
            }
        }

        ifNotNull(cameraSession, requestBuilder) { cameraSession, requestBuilder ->
            cameraSession.stopRepeating()

            //cancel any existing AF trigger (repeated touches, etc.)
            requestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL)
            requestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_OFF)
            cameraSession.capture(requestBuilder.build(), callback, null)

            //Now add a new AF trigger with focus region
            if (cameraInfo!!.isMeteringAreaAFSupported) {
                requestBuilder.set(CaptureRequest.CONTROL_AF_REGIONS, arrayOf(focusAreaTouch))
            }
            requestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO)
            requestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START)
            requestBuilder.setTag("FOCUS_TAG") //we'll capture this later for resuming the preview

            //then we ask for a single request (not repeating!)
            cameraSession.capture(requestBuilder.build(), callback, null)
        }

        manualFocusEngaged = true
    }

    fun zoom(touchDistance: Float): Float {
        val maxZoom = cameraInfo!!.maxZoom * 10
        val sensorArraySize = cameraInfo!!.sensorArraySize

        if(touchDistance > 0f) {
            when{
                touchDistance > priorZoomTouchDistance && maxZoom > zoomLevel -> zoomLevel+=zoomSensitivity
                touchDistance < priorZoomTouchDistance && zoomLevel > 1 -> zoomLevel-=zoomSensitivity
            }

            zoomLevel = zoomLevel.fitInRange(Range(minZoom, maxZoom))

            val minW = (sensorArraySize.width() / maxZoom).toInt()
            val minH = (sensorArraySize.height() / maxZoom).toInt()
            val difW = sensorArraySize.width() - minW
            val difH = sensorArraySize.height() - minH
            val cropW = (difW / 100 * zoomLevel).toInt()
            val cropH = (difH / 100 * zoomLevel).toInt()
            val zoom = Rect(cropW, cropH, sensorArraySize.width() - cropW, sensorArraySize.height() - cropH)
            requestBuilder!!.set(CaptureRequest.SCALER_CROP_REGION, zoom)
        }
        priorZoomTouchDistance = touchDistance

        cameraSession!!.setRepeatingRequest(requestBuilder!!.build(), null, null)

        return zoomLevel.reduceToRange(Range(minZoom, maxZoom), Range(minZoom, maxZoom / 10))
    }

    private fun getBackCameraInfo(): CameraInfo? {
        val cameraIds = cameraService.cameraIdList

        cameraIds.forEach { cameraId ->
            val cameraCharacteristics = cameraService.getCameraCharacteristics(cameraId)
            if(cameraCharacteristics[CameraCharacteristics.LENS_FACING] == CameraCharacteristics.LENS_FACING_BACK) {
                return CameraInfo(
                    id = cameraId,
                    isMeteringAreaAFSupported = (cameraCharacteristics[CameraCharacteristics.CONTROL_MAX_REGIONS_AF] as Int) >= 1,
                    sensorArraySize = cameraCharacteristics[CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE] as Rect,
                    maxZoom = cameraCharacteristics[CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM] as Float)
            }
        }

        return null
    }
}