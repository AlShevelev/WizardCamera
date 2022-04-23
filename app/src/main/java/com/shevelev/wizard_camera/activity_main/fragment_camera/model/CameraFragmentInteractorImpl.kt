package com.shevelev.wizard_camera.activity_main.fragment_camera.model

import com.shevelev.wizard_camera.core.camera_gl.api.CameraSettingsRepository
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.FiltersFacade
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.image_capture.ImageCapture
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.orientation.OrientationManager

class CameraFragmentInteractorImpl
constructor(
    override val filters: FiltersFacade,
    override val capture: ImageCapture,
    override val orientation: OrientationManager,
    override val cameraSettings: CameraSettingsRepository
) : CameraFragmentInteractor