package com.shevelev.wizard_camera.main_activity.view.widgets.filters_carousel

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.shevelev.wizard_camera.main_activity.dto.FilterFavoriteType
import com.shevelev.wizard_camera.main_activity.dto.FilterListItem
import com.shevelev.wizard_camera.main_activity.view_model.FilterEventsProcessor
import com.shevelev.wizard_camera.shared.glide.GlideTarget
import com.shevelev.wizard_camera.shared.glide.clear
import com.shevelev.wizard_camera.shared.glide.loadCircle
import kotlinx.android.synthetic.main.view_filters_carousel.view.*

class FiltersItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private var iconGlideTarget: GlideTarget? = null

    fun bind(item: FilterListItem, position: Int, eventsProcessor: FilterEventsProcessor) {
        iconGlideTarget = itemView.listItemIcon.loadCircle(item.staticData.icon)
        itemView.root.tag = FiltersItemTag(id = item.staticData.code, position = position)

        when(item.favorite) {
            FilterFavoriteType.FAVORITE -> {
                itemView.favoriteButton.visibility = View.VISIBLE
                itemView.favoriteButton.isActive = true
            }
            FilterFavoriteType.NOT_FAVORITE -> {
                itemView.favoriteButton.visibility = View.VISIBLE
                itemView.favoriteButton.isActive = false
            }
            FilterFavoriteType.UNKNOWN -> {
                itemView.favoriteButton.visibility = View.INVISIBLE
            }
        }

        itemView.favoriteButton.setOnPulseButtonClickListener { isActive ->
            eventsProcessor.onFavoriteFilterClick(item.staticData.code, isActive)
        }
    }

    fun recycle() {
        iconGlideTarget?.clear(itemView.context.applicationContext)
        itemView.favoriteButton.setOnPulseButtonClickListener(null)
    }
}