package com.shevelev.wizard_camera.main_activity.model

import com.shevelev.wizard_camera.main_activity.model.filters_repository.FiltersRepository
import com.shevelev.wizard_camera.main_activity.model.image_capture.ImageCapture
import com.shevelev.wizard_camera.shared.mvvm.model.ModelBaseImpl
import dagger.Lazy
import javax.inject.Inject

class MainActivityModelImpl
@Inject
constructor(
    override val filters: FiltersRepository,
    private val imageCapture: Lazy<ImageCapture>
) : ModelBaseImpl(),
    MainActivityModel {

    override val capture: ImageCapture
        get() = imageCapture.get()
}