package com.shevelev.wizard_camera.filters.display_data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode

data class FilterDisplayData(
    val code: GlFilterCode,
    @DrawableRes val icon: Int,
    @StringRes val title: Int
)