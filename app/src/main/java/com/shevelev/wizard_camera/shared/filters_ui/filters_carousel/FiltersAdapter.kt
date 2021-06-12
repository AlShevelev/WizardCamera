package com.shevelev.wizard_camera.shared.filters_ui.filters_carousel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class FiltersAdapter(
    @LayoutRes
    private val layoutId: Int,
    private val eventsProcessor: FilterEventsProcessor
) : RecyclerView.Adapter<FiltersItemViewHolder>() {

    private var items: List<FilterListItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiltersItemViewHolder =
        FiltersItemViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

    override fun onBindViewHolder(holder: FiltersItemViewHolder, position: Int) {
        val index = getItemIndexByPosition(position)

        holder.bind(items[index], position, eventsProcessor)
    }

    override fun onViewRecycled(holder: FiltersItemViewHolder) = holder.recycle()

    override fun getItemCount() = Int.MAX_VALUE

    fun setItems(newItems: List<FilterListItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    /**
     * Calculates scroll position by index of an item in the list
     * [position] an index in the list of items
     */
    fun recalculatePosition(position: Int): Int {
        val basePosition = Int.MAX_VALUE /2
        val baseIndex = getItemIndexByPosition(basePosition)

        return basePosition - (baseIndex - position) - Random.nextInt(1, 500)*items.size
    }

    private fun getItemIndexByPosition(position: Int) = position % items.size
}
