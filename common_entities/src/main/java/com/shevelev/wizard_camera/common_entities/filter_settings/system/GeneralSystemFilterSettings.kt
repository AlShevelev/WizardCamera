package com.shevelev.wizard_camera.common_entities.filter_settings.system

import com.shevelev.wizard_camera.common_entities.enums.SystemFilterCode

/**
 * Universal settings for all system filters
 */
data class GeneralSystemFilterSettings(
    override val code: SystemFilterCode,
    val filterValue: Float
) : SystemFilterSettings