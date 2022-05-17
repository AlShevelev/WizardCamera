package com.shevelev.wizard_camera.filters.filters_carousel

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode

class FiltersRecyclerView(
    context: Context,
    attrs: AttributeSet
) : RecyclerView(context, attrs) {

    private lateinit var filtersAdapter: FiltersAdapter

    fun init(items: List<FilterListItem>, eventsProcessor: FilterEventsProcessor) {
        if(!::filtersAdapter.isInitialized) {
            filtersAdapter = FiltersAdapter(R.layout.view_filters_carousel_item, eventsProcessor)

            layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
            adapter = filtersAdapter
        }

        filtersAdapter.setItems(items)

        items
            .indexOfFirst { it.isSelected }
            .let {
                if(it != -1) {
                    post {
                        (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(it, 0)
                    }
                }
            }
    }

    /**
     * Marks an item as selected
     */
    fun selectItem(selectedItemId: GlFilterCode) = filtersAdapter.selectItem(selectedItemId)

    fun setItemFavoriteStatus(itemId: GlFilterCode, isFavorite: Boolean) = filtersAdapter.setItemFavoriteStatus(itemId, isFavorite)

    fun scrollToItem(itemCode: GlFilterCode) {
        filtersAdapter.getItemPosition(itemCode)
            ?.let {
                (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(it, 0)
            }
    }
}