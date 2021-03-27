package com.shevelev.wizard_camera.main_activity.view.widgets.filters_carousel

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.main_activity.dto.FiltersListData
import com.shevelev.wizard_camera.main_activity.view_model.FilterEventsProcessor

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

    fun setStartData(data: FiltersListData, eventsProcessor: FilterEventsProcessor) {
        if(data.items.isEmpty()) {
            filtersList.visibility = View.INVISIBLE
            noItemsStub.visibility = View.VISIBLE
        } else {
            filtersList.visibility = View.VISIBLE
            noItemsStub.visibility = View.INVISIBLE

            filtersList.setStartData(data, eventsProcessor)
        }
    }

    fun scrollToPosition(position: Int) = filtersList.scrollToPosition(position)

    fun smoothScrollToPosition(position: Int) = filtersList.smoothScrollToPosition(position)

    fun setOnItemSelectedListener(listener: ((FilterCode) -> Unit)?) = filtersList.setOnItemSelectedListener(listener)
}