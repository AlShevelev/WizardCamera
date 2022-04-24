package com.shevelev.wizard_camera.core.camera_gl.api.bitmap.filters

import com.shevelev.wizard_camera.core.camera_gl.api.shared.GLFilterSettings
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode

interface GlSurfaceShaderFilter : GlSurfaceFilter {
    /**
     * A filter's code
     */
    val code: GlFilterCode

    fun updateSettings(settings: GLFilterSettings)
}