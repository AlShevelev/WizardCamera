package com.shevelev.wizard_camera.activity_main.fragment_camera.model

import com.shevelev.wizard_camera.core.camera_gl.api.CameraSettingsRepository
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.FiltersFacade
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.image_capture.ImageCapture
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.orientation.OrientationManager
import com.shevelev.wizard_camera.core.ui_utils.mvvm.model.InteractorBase

interface CameraFragmentInteractor : InteractorBase {
    val filters: FiltersFacade

    val capture: ImageCapture

    val orientation: OrientationManager

    val cameraSettings: CameraSettingsRepository
}