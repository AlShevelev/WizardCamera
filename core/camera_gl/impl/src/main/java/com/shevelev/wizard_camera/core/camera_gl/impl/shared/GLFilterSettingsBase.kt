package com.shevelev.wizard_camera.core.camera_gl.impl.shared

import com.shevelev.wizard_camera.core.camera_gl.api.shared.GLFilterSettings
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings

/**
 * Base class for passing [GlFilterSettings] params into OGL program
 */
internal abstract class GLFilterSettingsBase<T: GlFilterSettings>(protected val settings: T) : GLFilterSettings {
    override val code: GlFilterCode
        get() = settings.code
}