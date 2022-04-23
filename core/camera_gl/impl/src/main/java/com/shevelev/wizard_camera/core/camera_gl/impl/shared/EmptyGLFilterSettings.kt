package com.shevelev.wizard_camera.core.camera_gl.impl.shared

import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.EmptyFilterSettings

/**
 * A class for passing [EmptyFilterSettings] into an OGL filter
 */
class EmptyGLFilterSettings(
    settings: EmptyFilterSettings
) : GLFilterSettingsBase<EmptyFilterSettings>(settings) {
    /**
     * Passes params
     * @param program OGL program
     */
    override fun passSettingsParams(program: Int) {
        // do noting
    }
}