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

    fun scrollToItem(itemId: GlFilterCode) = filtersList.scrollToItem(itemId)

    /**
     * Marks an item as selected
     */
    fun selectItem(selectedItemId: GlFilterCode) = filtersList.selectItem(selectedItemId)

    fun setItemFavoriteStatus(itemId: GlFilterCode, isFavorite: Boolean) = filtersList.setItemFavoriteStatus(itemId, isFavorite)
}