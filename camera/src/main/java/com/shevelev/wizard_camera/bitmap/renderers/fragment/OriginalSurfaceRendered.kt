package com.shevelev.wizard_camera.bitmap.renderers.fragment

import android.content.Context
import android.graphics.Bitmap
import com.shevelev.wizard_camera.bitmap.renderers.GLSurfaceShaderRenderedBase
import com.shevelev.wizard_camera.camera.R

class OriginalSurfaceRendered(context: Context, bitmap: Bitmap): GLSurfaceShaderRenderedBase(context, bitmap, R.raw.original)