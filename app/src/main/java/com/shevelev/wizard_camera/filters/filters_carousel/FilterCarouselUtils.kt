package com.shevelev.wizard_camera.filters.filters_carousel

import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode

object FilterCarouselUtils {
    /**
     * Selects a new item in a list
     * @param source source items
     * @param selectedItem a code of a selected items
     * @return updated [source] list
     */
    fun setSelection(source: List<FilterListItem>, selectedItem: GlFilterCode): List<FilterListItem>? {
        val oldSelectedItemIndex = source.indexOfFirst { it.isSelected }

        if(oldSelectedItemIndex == -1 || source[oldSelectedItemIndex].displayData.code == selectedItem) {
            return null
        }

        val newSelectedItemIndex = source.indexOfFirst { it.displayData.code == selectedItem }

        if(newSelectedItemIndex == -1) {
            return null
        }

        val updatedSource = source.toMutableList()
        updatedSource[oldSelectedItemIndex] = updatedSource[oldSelectedItemIndex].copy(isSelected = false)
        updatedSource[newSelectedItemIndex] = updatedSource[newSelectedItemIndex].copy(isSelected = true)

        return updatedSource
    }

    /**
     * Sets/resets a favorite status for an item
     * @param source source items
     * @param itemCode processed item
     * @param isFavorite status to set
     * @return updated [source] list
     */
    fun setFavoriteStatus(source: List<FilterListItem>, itemCode: GlFilterCode, isFavorite: Boolean): List<FilterListItem>? {
        val itemPosition = source.indexOfFirst { it.displayData.code == itemCode }

        if(itemPosition == -1) {
            return null
        }

        val item = source[itemPosition]

        if(item.favorite == FilterFavoriteType.HIDDEN) {
            return null
        }

        val updatedSource = source.toMutableList()
        updatedSource[itemPosition] = item.copy(
            favorite = if(isFavorite) FilterFavoriteType.FAVORITE else FilterFavoriteType.NOT_FAVORITE
        )

        return updatedSource
    }
}