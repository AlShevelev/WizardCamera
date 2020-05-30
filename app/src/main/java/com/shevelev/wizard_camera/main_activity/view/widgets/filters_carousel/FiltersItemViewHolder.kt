package com.shevelev.wizard_camera.main_activity.view.widgets.filters_carousel

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.shevelev.wizard_camera.main_activity.dto.FiltersListItem
import com.shevelev.wizard_camera.shared.glide.GlideTarget
import com.shevelev.wizard_camera.shared.glide.clear
import com.shevelev.wizard_camera.shared.glide.loadCircle
import kotlinx.android.synthetic.main.view_filters_carousel.view.*

class FiltersItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private var iconGlideTarget: GlideTarget? = null

    fun bind(item: FiltersListItem, position: Int) {
        iconGlideTarget = itemView.listItemIcon.loadCircle(item.icon)
        itemView.root.tag = FiltersItemTag(id = item.code, position = position)
    }

    fun recycle() {
        iconGlideTarget?.clear(itemView.context.applicationContext)
    }
}