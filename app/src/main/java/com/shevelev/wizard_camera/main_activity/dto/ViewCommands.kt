package com.shevelev.wizard_camera.main_activity.dto

import com.shevelev.wizard_camera.shared.mvvm.view_commands.ViewCommand

class SetupCameraCommand: ViewCommand
class ReleaseCameraCommand: ViewCommand
class ReloadCameraCommand: ViewCommand

data class ShowCapturingSuccessCommand(val screenOrientation: ScreenOrientation): ViewCommand