package com.shevelev.wizard_camera.bitmap.filters.fragment

import android.content.Context
import android.graphics.Bitmap
import com.shevelev.wizard_camera.bitmap.filters.GLSurfaceShaderFilterBase
import com.shevelev.wizard_camera.camera.R

class OriginalSurfaceFilter(context: Context, bitmap: Bitmap): GLSurfaceShaderFilterBase(context, bitmap, R.raw.original)