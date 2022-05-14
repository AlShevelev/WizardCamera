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

    private var startId: GlFilterCode? = null

    private lateinit var filtersAdapter: FiltersAdapter

    fun setStartData(data: FiltersListData, eventsProcessor: FilterEventsProcessor) {
        startId = data.items[data.startPosition].displayData.code

        if(!::filtersAdapter.isInitialized) {
            filtersAdapter = FiltersAdapter(R.layout.view_filters_carousel_item, eventsProcessor)
            addAdapter(filtersAdapter)
        }

        filtersAdapter.setItems(data.items, data.startPosition)
        setUp(data.startPosition)
    }

    /**
     * Marks an item as selected
     */
    fun selectItem(selectedItemId: GlFilterCode) = filtersAdapter.selectItem(selectedItemId)

    fun scrollToItem(itemCode: GlFilterCode) {
        filtersAdapter.getItemPosition(itemCode)
            ?.let {
                (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(it, 0)
            }
    }

    private fun addAdapter(newAdapter: FiltersAdapter) {
        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        adapter = newAdapter
    }

    private fun setUp(position: Int) {
        post{
            (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 0)
        }
    }
}