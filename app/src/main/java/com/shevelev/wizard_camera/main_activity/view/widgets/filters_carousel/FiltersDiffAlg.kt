package com.shevelev.wizard_camera.main_activity.view.widgets.filters_carousel

import androidx.recyclerview.widget.DiffUtil
import com.shevelev.wizard_camera.main_activity.dto.FilterListItem

class FiltersDiffAlg(
    private val oldList: List<FilterListItem>,
    private val newList: List<FilterListItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].displayData.code == newList[newItemPosition].displayData.code

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        areItemsTheSame(oldItemPosition, newItemPosition) &&
            oldList[oldItemPosition].favorite == newList[newItemPosition].favorite
}