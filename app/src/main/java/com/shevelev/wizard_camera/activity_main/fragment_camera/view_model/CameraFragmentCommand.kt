package com.shevelev.wizard_camera.activity_main.fragment_camera.view_model

import androidx.annotation.StringRes
import androidx.camera.core.impl.ImageOutputConfig
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto.ScreenOrientation
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import java.io.OutputStream

internal sealed class CameraFragmentCommand {
    data class ShowMessageRes(
        @StringRes val textResId: Int,
        val isError: Boolean = true
    ) : CameraFragmentCommand()

    data class StartCapture(
        val targetStream: OutputStream,
        val isFlashLightActive: Boolean,
        @ImageOutputConfig.RotationValue
        val rotation: Int
    ) : CameraFragmentCommand()

    data class ShowCapturingSuccess(
        val screenOrientation: ScreenOrientation
    ) : CameraFragmentCommand()

    object ResetExposure : CameraFragmentCommand()

    data class SetExposure(
        val exposureValue: Float
    ) : CameraFragmentCommand()

    object NavigateToGallery : CameraFragmentCommand()

    data class SetFlowerMenuVisibility(
        val isVisible: Boolean
    ) : CameraFragmentCommand()

    data class Exit(
        @StringRes val messageResId: Int
    ) : CameraFragmentCommand()

    data class ShowFilterSettings(
        val settings: GlFilterSettings
    ) : CameraFragmentCommand()

    data class Zoom(
        val scaleFactor: Float
    ) : CameraFragmentCommand()

    object HideFilterSettings : CameraFragmentCommand()
}