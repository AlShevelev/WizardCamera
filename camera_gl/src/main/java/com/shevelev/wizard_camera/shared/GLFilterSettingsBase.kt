package com.shevelev.wizard_camera.shared

import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings

/**
 * Base class for passing [FilterSettings] params into OGL program
 */
abstract class GLFilterSettingsBase<T: FilterSettings>(protected val settings: T) : GLFilterSettings