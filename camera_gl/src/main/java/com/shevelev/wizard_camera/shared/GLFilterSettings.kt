package com.shevelev.wizard_camera.shared

import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings

/**
 * An interface for passing [GlFilterSettings] params into OGL program
 */
interface GLFilterSettings {
    /**
     * Code of the filter
     */
    val code: GlFilterCode

    /**
     * Passes params
     * @param program OGL program
     */
    fun passSettingsParams(program: Int)
}