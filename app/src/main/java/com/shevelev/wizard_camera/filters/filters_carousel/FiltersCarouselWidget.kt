package com.shevelev.wizard_camera.filters.filters_carousel

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode

class FiltersCarouselWidget
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val filtersList by lazy { findViewById<FiltersRecyclerView>(R.id.filtersList) }
    private val noItemsStub by lazy { findViewById<TextView>(R.id.noItemsStub) }

    init {
        inflate(context, R.layout.view_filters_carousel, this)
    }

    fun updateData(items: List<FilterListItem>, eventsProcessor: FilterEventsProcessor) {
        if(items.isEmpty()) {
            filtersList.visibility = View.INVISIBLE
            noItemsStub.visibility = View.VISIBLE
        } else {
            filtersList.visibility = View.VISIBLE
            noItemsStub.visibility = View.INVISIBLE

            filtersList.updateData(items, eventsProcessor)
        }
    }

    fun scrollToItem(itemId: GlFilterCode) = filtersList.scrollToItem(itemId)
}