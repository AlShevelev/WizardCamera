package com.shevelev.wizard_camera.filters.display_data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class FilterDisplayData(
    val id: FilterDisplayId,
    @DrawableRes val icon: Int,
    @StringRes val title: Int
)