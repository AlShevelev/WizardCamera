package com.shevelev.wizard_camera.filters.filters_carousel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode

class FiltersAdapter(
    @LayoutRes
    private val layoutId: Int,
    private val eventsProcessor: FilterEventsProcessor
) : RecyclerView.Adapter<FiltersItemViewHolder>() {

    private var items: MutableList<FilterListItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiltersItemViewHolder =
        FiltersItemViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

    override fun onBindViewHolder(holder: FiltersItemViewHolder, position: Int) {
        holder.bind(items[position], position, eventsProcessor)
    }

    override fun onViewRecycled(holder: FiltersItemViewHolder) = holder.recycle()

    override fun getItemCount() = items.size

    fun setItems(newItems: List<FilterListItem>) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }

    /**
     * Marks an item as selected
     */
    fun selectItem(selectedItemId: GlFilterCode) {
        val oldSelectedItemPosition = items.indexOfFirst { it.isSelected }
        val newSelectedItemPosition = items.indexOfFirst { it.displayData.code == selectedItemId }

        if(oldSelectedItemPosition == -1 || newSelectedItemPosition == -1) {
            return
        }

        val oldSelectedItem = items[oldSelectedItemPosition]
        val newSelectedItem = items[newSelectedItemPosition]

        if(oldSelectedItem.displayData.code == newSelectedItem.displayData.code) {
            return
        }

        items[oldSelectedItemPosition] = oldSelectedItem.copy(isSelected = false)
        notifyItemChanged(oldSelectedItemPosition)

        items[newSelectedItemPosition] = newSelectedItem.copy(isSelected = true)
        notifyItemChanged(newSelectedItemPosition)
    }

    fun setItemFavoriteStatus(itemId: GlFilterCode, isFavorite: Boolean) {
        val itemIndex = items.indexOfFirst { it.displayData.code == itemId }

        if(itemIndex != -1) {
            val item = items[itemIndex]

            if(item.favorite != FilterFavoriteType.HIDDEN) {
                val newItem = item.copy(
                    favorite = if(isFavorite) FilterFavoriteType.FAVORITE else FilterFavoriteType.NOT_FAVORITE
                )

                if(newItem.favorite != item.favorite) {
                    items[itemIndex] = newItem      // We don't need to call notifyItemChanged here
                }
            }
        }
    }

    fun getItemPosition(itemCode: GlFilterCode): Int? =
        items
            .indexOfFirst { it.displayData.code == itemCode }
            .let { if(it == -1) null else it }
}
