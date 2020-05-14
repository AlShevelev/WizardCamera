package com.shevelev.wizard_camera.main_activity.model

import com.shevelev.wizard_camera.main_activity.model.filters_repository.FiltersRepository
import com.shevelev.wizard_camera.shared.mvvm.model.ModelBaseImpl
import javax.inject.Inject

class MainActivityModelImpl
@Inject
constructor(
    override val filters: FiltersRepository
) : ModelBaseImpl(), MainActivityModel