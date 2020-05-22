package com.shevelev.wizard_camera.common_entities.entities

import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import org.threeten.bp.ZonedDateTime

/**
 * One photo shot
 */
data class PhotoShot (
    val id: Long,
    val fileName: String,
    val created: ZonedDateTime,
    val filter: FilterCode
)