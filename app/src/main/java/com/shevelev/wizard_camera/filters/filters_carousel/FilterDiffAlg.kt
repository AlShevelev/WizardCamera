package com.shevelev.wizard_camera.filters.filters_carousel

import androidx.recyclerview.widget.DiffUtil

class FilterDiffAlg(
    private val oldList: List<FilterListItem>,
    private val newList: List<FilterListItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.displayData.code == newItem.displayData.code && oldItem.listId == newItem.listId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem == newItem
    }
}