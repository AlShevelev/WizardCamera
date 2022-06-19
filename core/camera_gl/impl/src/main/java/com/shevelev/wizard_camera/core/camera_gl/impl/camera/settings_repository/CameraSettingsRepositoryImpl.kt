package com.shevelev.wizard_camera.core.camera_gl.impl.camera.settings_repository

import android.content.Context
import android.graphics.ImageFormat
import android.graphics.Point
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.util.Size
import android.view.WindowManager
import kotlin.math.abs

internal class CameraSettingsRepositoryImpl(
    private val appContext: Context
) : CameraSettingsRepository {

    override val optimalOutputSize by lazy { calculateOptimalOutputSize() }

    private fun calculateOptimalOutputSize(): Size {
        val cameraService = appContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        val cameraIds = cameraService.cameraIdList

        cameraIds.forEach { cameraId ->
            val cameraCharacteristics = cameraService.getCameraCharacteristics(cameraId)
            if (cameraCharacteristics[CameraCharacteristics.LENS_FACING] == CameraCharacteristics.LENS_FACING_BACK) {
                val configurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

                val outputSize = configurationMap!!.getOutputSizes(ImageFormat.JPEG).toList()

                return selectOptimalOutputSize(outputSize)
            }
        }

        throw UnsupportedOperationException("Can't find back camera")
    }

    private fun selectOptimalOutputSize(sourceOutputSize: List<Size>): Size {
        val realScreenSize = calculateRealScreenSize()

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
}