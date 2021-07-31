package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view.adapter

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.NO_ID
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.dto.GalleryItem
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view_model.GalleryPagingActions
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery_page.GalleryPageFragment

class GalleryAdapter(
    private val fragment: Fragment,
    private val pageSize: Int,
    private val galleryPaging: GalleryPagingActions
) : FragmentStateAdapter(fragment) {
    private var items = listOf<GalleryItem>()

    fun updateItems(newItems: List<GalleryItem>) {
        val diffCallback = GalleryDiffAlg(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = newItems.toList()
        diffResult.dispatchUpdatesTo(this)
    }

    override fun createFragment(position: Int): Fragment {
        if (position > items.size - pageSize / 2) {
            galleryPaging.loadNextPage()
        }

        return GalleryPageFragment.newInstance(items[position])
    }

    override fun onBindViewHolder(holder: FragmentViewHolder, position: Int, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty()) {
            getFragment (holder.itemId)?.apply {
                loadPhoto(items[position])
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemId(position: Int): Long = if (position < 0) NO_ID else items[position].id

    override fun containsItem(itemId: Long): Boolean = items.any { it.id == itemId }

    override fun getItemCount(): Int = items.size

    fun getFragment(position: Int): GalleryPageFragment? = getFragment(getItemId(position))

    private fun getFragment(id: Long): GalleryPageFragment? =
        // It's a dirty hack, I know. But I'm afraid there is no other way
        fragment.childFragmentManager.findFragmentByTag("f$id") as? GalleryPageFragment
}