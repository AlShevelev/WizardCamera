package com.shevelev.wizard_camera.main_activity.view.widgets.filters_carousel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.shevelev.wizard_camera.main_activity.dto.FiltersListItem

class FiltersAdapter(
    @LayoutRes
    private val layoutId: Int
) : RecyclerView.Adapter<FiltersItemViewHolder>() {

    private var items: List<FiltersListItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiltersItemViewHolder =
        FiltersItemViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

    override fun onBindViewHolder(holder: FiltersItemViewHolder, position: Int) {
        val index = getItemIndexByPosition(position)

        holder.bind(items[index], position)
    }

    override fun onViewRecycled(holder: FiltersItemViewHolder) = holder.recycle()

    override fun getItemCount() = Int.MAX_VALUE

    fun setItems(newItems: List<FiltersListItem>) {
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

        return basePosition - (baseIndex - position)
    }

    private fun getItemIndexByPosition(position: Int) = position % items.size
}
