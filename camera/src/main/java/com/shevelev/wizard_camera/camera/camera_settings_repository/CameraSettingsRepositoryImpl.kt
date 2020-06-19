package com.shevelev.wizard_camera.camera.camera_settings_repository

import android.content.Context
import android.graphics.ImageFormat
import android.graphics.Point
import android.graphics.Rect
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.util.Range
import android.util.Rational
import android.util.Size
import android.view.WindowManager
import com.shevelev.wizard_camera.common_entities.di_scopes.ApplicationScope
import java.lang.UnsupportedOperationException
import javax.inject.Inject
import kotlin.math.abs

@ApplicationScope
class CameraSettingsRepositoryImpl
@Inject
constructor(
    private val appContext: Context
) : CameraSettingsRepository {
    // To avoid to use Dagger Lazy
    private val cameraInfo: CameraInfo by lazy { calculateCameraInfo() }

    private val realScreenSize: Size by lazy { calculateRealScreenSize() }

    override val cameraId = cameraInfo.id

    override val isMeteringAreaAFSupported = cameraInfo.isMeteringAreaAFSupported

    override val sensorArraySize = cameraInfo.sensorArraySize

    override val maxZoom = cameraInfo.maxZoom

    override val exposureRange = cameraInfo.exposureRange

    override val exposureStep = cameraInfo.exposureStep

    override val optimalOutputSize = cameraInfo.optimalOutputSize

    override val screenTextureSize by lazy { calculateScreenTextureHeight() }

    private fun calculateCameraInfo(): CameraInfo {
        val cameraService = appContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        val cameraIds = cameraService.cameraIdList

        cameraIds.forEach { cameraId ->
            val cameraCharacteristics = cameraService.getCameraCharacteristics(cameraId)
            if (cameraCharacteristics[CameraCharacteristics.LENS_FACING] == CameraCharacteristics.LENS_FACING_BACK) {
                val configurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

                val outputSize = configurationMap!!.getOutputSizes(ImageFormat.JPEG).toList()

                return CameraInfo(
                    id = cameraId,
                    isMeteringAreaAFSupported = (cameraCharacteristics[CameraCharacteristics.CONTROL_MAX_REGIONS_AF] as Int) >= 1,
                    sensorArraySize = cameraCharacteristics[CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE] as Rect,
                    maxZoom = cameraCharacteristics[CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM] as Float,
                    exposureRange = cameraCharacteristics[CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE] as Range<Int>,
                    exposureStep = cameraCharacteristics[CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP] as Rational,
                    outputSize = outputSize,
                    optimalOutputSize = calculateOptimalOutputSize(outputSize)
                )
            }
        }

        throw UnsupportedOperationException("Can't find back camera")
    }

    private fun calculateOptimalOutputSize(sourceOutputSize: List<Size>): Size {
        val candidates = mutableListOf<Pair<Int, Size>>()

        sourceOutputSize.forEach { outputSize ->
            val relativeScreenHeight = ((outputSize.height / realScreenSize.width.toFloat()) * realScreenSize.height).toInt()
            candidates.add(Pair(abs(relativeScreenHeight - outputSize.width), outputSize))
        }

        val resultCandidates = candidates
            .sortedWith(compareBy({ it.first }, { it.second.width }))
            .map { it.second }

        resultCandidates.forEach {
            if(it.width > realScreenSize.height) {
                return it
            }
        }
        return resultCandidates.last()
    }

    private fun calculateRealScreenSize(): Size {
        val windowsService = appContext.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowsService.defaultDisplay

        val realSize = Point()
        display.getRealSize(realSize)
        return Size(realSize.x, realSize.y)
    }

    private fun calculateScreenTextureHeight(): Size {
        val relativeHeight = ((realScreenSize.width.toFloat() / optimalOutputSize.height) * optimalOutputSize.width).toInt()
        val textureHeight = if(relativeHeight > realScreenSize.height) realScreenSize.height  else relativeHeight
        return Size(realScreenSize.width, textureHeight)
    }
}