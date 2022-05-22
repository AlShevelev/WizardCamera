package com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shevelev.wizard_camera.core.ui_kit.lib.R

class FiltersRecyclerView(
    context: Context,
    attrs: AttributeSet
) : RecyclerView(context, attrs) {

    private lateinit var filtersAdapter: FiltersAdapter

    fun updateData(items: List<FilterListItem>, eventsProcessor: FilterEventsProcessor) {
        if(!::filtersAdapter.isInitialized) {
            filtersAdapter = FiltersAdapter(R.layout.view_filters_carousel_item, eventsProcessor)

            layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
            adapter = filtersAdapter
        }

        val isNewListType = filtersAdapter.setItems(items)

        // If new type of list has been loaded to the carousel
        if(isNewListType) {
            items
                .indexOfFirst { it.isSelected }
                .let {
                    if (it != -1) {
                        post { (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(it, 0) }
                    }
                }
        }
    }
}