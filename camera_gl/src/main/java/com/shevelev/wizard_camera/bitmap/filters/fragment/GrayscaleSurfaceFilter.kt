package com.shevelev.wizard_camera.bitmap.filters.fragment

import android.content.Context
import android.graphics.Bitmap
import android.util.Size
import com.shevelev.wizard_camera.bitmap.filters.GLSurfaceShaderFilterBase
import com.shevelev.wizard_camera.camera.R
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings

class GrayscaleSurfaceFilter(
    context: Context,
    bitmap: Bitmap,
    screenSize: Size,
    settings: FilterSettings
) : GLSurfaceShaderFilterBase(
    context,
    bitmap,
    R.raw.gray,
    screenSize,
    settings)
