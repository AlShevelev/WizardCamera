package com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class FiltersAdapter(
    @LayoutRes
    private val layoutId: Int,
    private val eventsProcessor: FilterEventsProcessor
) : RecyclerView.Adapter<FiltersItemViewHolder>() {

    private var items = listOf<FilterListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiltersItemViewHolder =
        FiltersItemViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

    override fun onBindViewHolder(holder: FiltersItemViewHolder, position: Int) {
        holder.bind(items[position], position, eventsProcessor)
    }

    override fun onViewRecycled(holder: FiltersItemViewHolder) = holder.recycle()

    override fun getItemCount() = items.size

    fun setItems(newItems: List<FilterListItem>): Boolean {
        val oldListId = items.firstOrNull()?.listId
        val newListId = newItems.firstOrNull()?.listId

        val diffCallback = FilterDiffAlg(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = newItems.toList()
        diffResult.dispatchUpdatesTo(this)

        return oldListId != newListId
    }
}
