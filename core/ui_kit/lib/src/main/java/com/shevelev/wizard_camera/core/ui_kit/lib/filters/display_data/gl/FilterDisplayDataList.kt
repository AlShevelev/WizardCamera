package com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.gl

import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.display_data.FilterDisplayData

interface FilterDisplayDataList {
    operator fun get(code: GlFilterCode): FilterDisplayData

    fun exists(code: GlFilterCode): Boolean
}