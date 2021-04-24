package com.shevelev.wizard_camera.shared

import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings

/**
 * An interface for passing [FilterSettings] params into OGL program
 */
interface GLFilerSettings {
    /**
     * Passes params
     * @param program OGL program
     */
    fun passSettingsParams(program: Int)
}