package com.shevelev.wizard_camera.activity_main.fragment_camera.model

import com.shevelev.wizard_camera.camera.settings_repository.CameraSettingsRepository
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.filters_facade.FiltersFacade
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.image_capture.ImageCapture
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.orientation.OrientationManager
import dagger.Lazy
import javax.inject.Inject

class CameraFragmentInteractorImpl
@Inject
constructor(
    override val filters: FiltersFacade,
    private val imageCapture: Lazy<ImageCapture>,
    override val orientation: OrientationManager,
    override val cameraSettings: CameraSettingsRepository
) : CameraFragmentInteractor {

    override val capture: ImageCapture
        get() = imageCapture.get()
}