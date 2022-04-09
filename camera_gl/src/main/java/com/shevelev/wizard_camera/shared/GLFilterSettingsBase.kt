package com.shevelev.wizard_camera.shared

import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings

/**
 * Base class for passing [GlFilterSettings] params into OGL program
 */
abstract class GLFilterSettingsBase<T: GlFilterSettings>(protected val settings: T) : GLFilterSettings {
    override val code: GlFilterCode
        get() = settings.code
}