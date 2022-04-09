package com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto

import androidx.annotation.StringRes
import androidx.camera.core.impl.ImageOutputConfig
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ViewCommand
import java.io.File

class ShowCapturingSuccessCommand(val screenOrientation: ScreenOrientation): ViewCommand

/**
 * @param targetFile file for a captured image
 */
class StartCaptureCommand(
    val targetFile: File,
    val isFlashLightActive: Boolean,
    @ImageOutputConfig.RotationValue
    val rotation: Int
) : ViewCommand

class ZoomCommand(val scaleFactor: Float): ViewCommand

class ResetExposureCommand: ViewCommand
class SetExposureCommand(val exposureValue: Float): ViewCommand

class NavigateToGalleryCommand: ViewCommand

class ExitCommand(@StringRes val messageResId: Int): ViewCommand

class ShowFilterSettingsCommand(val settings: GlFilterSettings): ViewCommand
class HideFilterSettingsCommand : ViewCommand