package com.shevelev.wizard_camera.core.common_entities.enums

enum class FiltersGroup(val index: Int) {
    NO_FILTERS(0),
    ALL(1),
    STYLIZATION(2),
    DEFORMATIONS(3),
    COLORS(4),
    FAVORITES(5);

    companion object {
        fun fromIndex(index: Int): FiltersGroup? = values().firstOrNull { it.index == index }
    }
}