package com.shevelev.wizard_camera.shared.filters_ui.display_data

import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.common_entities.enums.SystemFilterCode

/**
 * An independent Id for displayed filer */
class FilterDisplayId private constructor(val id: Int) {
    val filterCode: GlFilterCode
        get () = GlFilterCode.values().first { it.value == id }

    val systemFilterCode: SystemFilterCode
        get() = SystemFilterCode.values().first { it.value == id }

    companion object {
        fun create(filterCode: GlFilterCode) = FilterDisplayId(filterCode.value)
        fun create(filterCode: SystemFilterCode) = FilterDisplayId(filterCode.value)
    }
}
