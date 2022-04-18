package com.shevelev.wizard_camera.filters.display_data

import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode

/**
 * An independent Id for displayed filer */
class FilterDisplayId private constructor(val id: Int) {
    val filterCode: GlFilterCode
        get () = GlFilterCode.values().first { it.value == id }

    companion object {
        fun create(filterCode: GlFilterCode) = FilterDisplayId(filterCode.value)
    }
}
