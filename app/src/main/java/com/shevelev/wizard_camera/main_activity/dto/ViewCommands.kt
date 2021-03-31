package com.shevelev.wizard_camera.main_activity.dto

import androidx.annotation.StringRes
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ViewCommand

class SetupCameraCommand: ViewCommand
class ReleaseCameraCommand: ViewCommand

data class SetFlashStateCommand(val turnFlashOn: Boolean): ViewCommand

data class ShowCapturingSuccessCommand(val screenOrientation: ScreenOrientation): ViewCommand

data class ZoomCommand(val scaleFactor: Float): ViewCommand

class ResetExposureCommand: ViewCommand
data class SetExposureCommand(val exposureValue: Float): ViewCommand

class NavigateToGalleryCommand: ViewCommand

data class ExitCommand(@StringRes val messageResId: Int): ViewCommand

data class ShowFilterSettingsCommand(val settings: FilterSettings): ViewCommand
class HideFilterSettingsCommand(): ViewCommand