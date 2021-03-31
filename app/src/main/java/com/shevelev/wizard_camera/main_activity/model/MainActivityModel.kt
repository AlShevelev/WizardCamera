package com.shevelev.wizard_camera.main_activity.model

import com.shevelev.wizard_camera.camera.camera_settings_repository.CameraSettingsRepository
import com.shevelev.wizard_camera.main_activity.model.filters_facade.FiltersFacade
import com.shevelev.wizard_camera.main_activity.model.image_capture.ImageCapture
import com.shevelev.wizard_camera.main_activity.model.orientation.OrientationManager
import com.shevelev.wizard_camera.shared.mvvm.model.ModelBase

interface MainActivityModel : ModelBase {
    val filters: FiltersFacade

    val capture: ImageCapture

    val orientation: OrientationManager

    val cameraSettings: CameraSettingsRepository
}