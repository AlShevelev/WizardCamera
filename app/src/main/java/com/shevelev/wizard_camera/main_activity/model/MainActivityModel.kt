package com.shevelev.wizard_camera.main_activity.model

import com.shevelev.wizard_camera.main_activity.model.filters_repository.FiltersRepository
import com.shevelev.wizard_camera.main_activity.model.image_capture.ImageCapture
import com.shevelev.wizard_camera.main_activity.model.orientation.OrientationManager
import com.shevelev.wizard_camera.shared.mvvm.model.ModelBase

interface MainActivityModel : ModelBase {
    val filters: FiltersRepository

    val capture: ImageCapture

    val orientation: OrientationManager
}