package com.shevelev.wizard_camera.main_activity.model

import android.util.Size
import com.shevelev.wizard_camera.camera.camera_settings_repository.CameraSettingsRepository
import com.shevelev.wizard_camera.main_activity.model.filters_facade.FiltersFacade
import com.shevelev.wizard_camera.main_activity.model.image_capture.ImageCapture
import com.shevelev.wizard_camera.main_activity.model.orientation.OrientationManager
import com.shevelev.wizard_camera.shared.mvvm.model.ModelBaseImpl
import dagger.Lazy
import javax.inject.Inject

class MainActivityModelImpl
@Inject
constructor(
    override val filters: FiltersFacade,
    private val imageCapture: Lazy<ImageCapture>,
    override val orientation: OrientationManager,
    override val cameraSettings: CameraSettingsRepository
) : ModelBaseImpl(), MainActivityModel {

    override val capture: ImageCapture
        get() = imageCapture.get()
}