package com.shevelev.wizard_camera.main_activity.view.widgets.filters_carousel

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.shevelev.wizard_camera.main_activity.dto.FilterFavoriteType
import com.shevelev.wizard_camera.main_activity.dto.FilterListItem
import com.shevelev.wizard_camera.main_activity.view_model.FilterEventsProcessor
import com.shevelev.wizard_camera.shared.glide.GlideTarget
import com.shevelev.wizard_camera.shared.glide.clear
import com.shevelev.wizard_camera.shared.glide.loadCircle
import kotlinx.android.synthetic.main.view_filters_carousel_item.view.*

class FiltersItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private var iconGlideTarget: GlideTarget? = null

    fun bind(item: FilterListItem, position: Int, eventsProcessor: FilterEventsProcessor) {
        iconGlideTarget = itemView.listItemIcon.loadCircle(item.displayData.icon)
        itemView.root.tag = FiltersItemTag(id = item.displayData.code, position = position)

        when(item.favorite) {
            FilterFavoriteType.FAVORITE -> {
                itemView.favoriteButton.visibility = View.VISIBLE
                itemView.favoriteButton.isActive = true
            }
            FilterFavoriteType.NOT_FAVORITE -> {
                itemView.favoriteButton.visibility = View.VISIBLE
                itemView.favoriteButton.isActive = false
            }
            FilterFavoriteType.HIDDEN -> {
                itemView.favoriteButton.visibility = View.INVISIBLE
            }
        }

        itemView.settingsButton.visibility = if(item.hasSettings) View.VISIBLE else View.INVISIBLE

        itemView.favoriteButton.setOnPulseButtonClickListener { isActive ->
            eventsProcessor.onFavoriteFilterClick(item.displayData.code, isActive)
        }

        itemView.settingsButton.setOnPulseButtonClickListener {
            eventsProcessor.onSettingsClick(item.displayData.code)
        }
    }

    fun recycle() {
        iconGlideTarget?.clear(itemView.context.applicationContext)
        itemView.favoriteButton.setOnPulseButtonClickListener(null)
        itemView.settingsButton.setOnPulseButtonClickListener(null)
    }
}