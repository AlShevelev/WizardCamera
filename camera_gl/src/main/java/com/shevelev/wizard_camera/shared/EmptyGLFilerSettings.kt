package com.shevelev.wizard_camera.shared

import com.shevelev.wizard_camera.common_entities.filter_settings.EmptyFilterSettings

/**
 * A class for passing [EmptyFilterSettings] into an OGL filter
 */
class EmptyGLFilerSettings(
    settings: EmptyFilterSettings
) : GLFilerSettingsBase<EmptyFilterSettings>(settings) {
    /**
     * Passes params
     * @param program OGL program
     */
    override fun passSettingsParams(program: Int) {
        // do noting
    }
}