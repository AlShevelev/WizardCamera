package com.shevelev.wizard_camera.shared.filters_ui.dto

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class FilterDisplayData(
    val id: FilterDisplayId,
    @DrawableRes val icon: Int,
    @StringRes val title: Int
)