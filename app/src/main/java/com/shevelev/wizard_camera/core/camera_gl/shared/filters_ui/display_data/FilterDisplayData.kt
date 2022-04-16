package com.shevelev.wizard_camera.core.camera_gl.shared.filters_ui.display_data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class FilterDisplayData(
    val id: FilterDisplayId,
    @DrawableRes val icon: Int,
    @StringRes val title: Int
)