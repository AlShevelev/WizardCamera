package com.shevelev.wizard_camera.main_activity.dto

import android.graphics.PointF
import android.util.SizeF
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ViewCommand

class SetupCameraCommand: ViewCommand
class ReleaseCameraCommand: ViewCommand

data class SetFlashStateCommand(val turnFlashOn: Boolean): ViewCommand

data class ShowCapturingSuccessCommand(val screenOrientation: ScreenOrientation): ViewCommand

data class FocusOnTouchCommand(val touchPoint: PointF, val touchAreaSize: SizeF): ViewCommand
class AutoFocusCommand : ViewCommand

data class ZoomCommand(val touchDistance: Float): ViewCommand

class ResetExposureCommand: ViewCommand
data class SetExposureCommand(val exposureValue: Float): ViewCommand

class NavigateToGalleryCommand: ViewCommand