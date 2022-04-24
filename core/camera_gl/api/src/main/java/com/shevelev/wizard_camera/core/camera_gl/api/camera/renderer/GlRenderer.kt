package com.shevelev.wizard_camera.core.camera_gl.api.camera.renderer

import android.graphics.SurfaceTexture
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings

interface GlRenderer {
    val cameraSurfaceTexture: SurfaceTexture

    fun initGL(texture: SurfaceTexture, glWidth: Int, glHeight: Int)

    fun setFilter(filterSettings: GlFilterSettings)

    fun releaseGL()
}