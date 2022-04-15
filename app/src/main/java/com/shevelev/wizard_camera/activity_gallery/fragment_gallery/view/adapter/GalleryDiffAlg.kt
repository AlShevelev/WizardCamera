package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.dto.GalleryItem

class GalleryDiffAlg(
    private val oldList: List<GalleryItem>,
    private val newList: List<GalleryItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id &&
            oldList[oldItemPosition].version == newList[newItemPosition].version

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any = newList[newItemPosition].item
}