package com.shevelev.wizard_camera.activity_main.fragment_camera.model

import com.shevelev.wizard_camera.feature.filters_facade.api.FiltersFacade
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.image_capture.ImageCapture
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.orientation.OrientationManager
import com.shevelev.wizard_camera.core.ui_utils.mvvm.model.InteractorBase

interface CameraFragmentInteractor : InteractorBase {
    val filters: FiltersFacade

    val capture: ImageCapture

    val orientation: OrientationManager
}