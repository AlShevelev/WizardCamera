package com.shevelev.wizard_camera.core.common_entities.filter_settings.gl

import android.os.Parcelable
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode

/**
 * Base interface for OpenGL filers
 */
interface GlFilterSettings : Parcelable {
    val code: GlFilterCode
}