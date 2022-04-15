package com.shevelev.wizard_camera.core.common_entities.filter_settings

/**
 * Base interface for all filters
 */
interface FilterSettings<T: Enum<T>> {
    val code: T
}