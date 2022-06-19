package com.shevelev.wizard_camera.activity_main.fragment_camera.model

import com.shevelev.wizard_camera.feature.filters_facade.api.FiltersFacade
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.image_capture.ImageCapture
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.orientation.OrientationManager

class CameraFragmentInteractorImpl(
    override val filters: FiltersFacade,
    override val capture: ImageCapture,
    override val orientation: OrientationManager
) : CameraFragmentInteractor