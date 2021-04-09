package com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto

import androidx.annotation.StringRes
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ViewCommand
import java.io.File

class SetupCameraCommand: ViewCommand
class ReleaseCameraCommand: ViewCommand

data class ShowCapturingSuccessCommand(val screenOrientation: ScreenOrientation): ViewCommand

/**
 * @param targetFile file for a captured image
 */
data class StartCaptureCommand(
    val targetFile: File,
    val isFlashLightActive: Boolean
) : ViewCommand

data class ZoomCommand(val scaleFactor: Float): ViewCommand

class ResetExposureCommand: ViewCommand
data class SetExposureCommand(val exposureValue: Float): ViewCommand

class NavigateToGalleryCommand: ViewCommand

data class ExitCommand(@StringRes val messageResId: Int): ViewCommand

data class ShowFilterSettingsCommand(val settings: FilterSettings): ViewCommand
class HideFilterSettingsCommand(): ViewCommand