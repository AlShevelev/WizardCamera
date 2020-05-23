package com.shevelev.wizard_camera.gallery_activity.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.NO_ID
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.gallery_activity.view_model.GalleryPagingActions
import com.shevelev.wizard_camera.gallery_page.GalleryPageFragment

class GalleryAdapter(
    activity: FragmentActivity,
    private val pageSize: Int,
    private val galleryPaging: GalleryPagingActions
) : FragmentStateAdapter(activity) {
    private var items = listOf<PhotoShot>()

    fun updateItems(newItems: List<PhotoShot>) {
        val diffCallback = GalleryDiffAlg(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = newItems.toList()
        diffResult.dispatchUpdatesTo(this)
    }

    override fun createFragment(position: Int): Fragment {
        if (position > items.size - pageSize / 2) {
            galleryPaging.loadPage()
        }

        return GalleryPageFragment.newInstance(items[position])
    }

    override fun getItemId(position: Int): Long = if (position < 0) NO_ID else items[position].id

    override fun containsItem(itemId: Long): Boolean = items.any { it.id == itemId }

    override fun getItemCount(): Int = items.size
}