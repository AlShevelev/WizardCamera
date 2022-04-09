package com.shevelev.wizard_camera.shared.filters_ui.display_data

import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode

/**
 * An independent Id for displayed filer */
class FilterDisplayId private constructor(val id: Int) {
    val filterCode: GlFilterCode
        get () = GlFilterCode.values().first { it.value == id }

    companion object {
        fun create(filterCode: GlFilterCode) = FilterDisplayId(filterCode.value)
    }
}
