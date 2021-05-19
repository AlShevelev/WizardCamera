package com.shevelev.wizard_camera.common_entities.filter_settings

/**
 * Base interface for all filters
 */
interface FilterSettings<T: Enum<T>> {
    val code: T
}