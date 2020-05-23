package com.shevelev.wizard_camera.gallery_activity.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot

class GalleryDiffAlg(
    private val oldList: List<PhotoShot>,
    private val newList: List<PhotoShot>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id
}