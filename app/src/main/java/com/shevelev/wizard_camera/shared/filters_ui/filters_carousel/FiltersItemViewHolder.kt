package com.shevelev.wizard_camera.shared.filters_ui.filters_carousel

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.activity_main.fragment_camera.view.widgets.pulse_image_button.PulseImageButton
import com.shevelev.wizard_camera.activity_main.fragment_camera.view.widgets.pulse_image_button.PulseImageButtonStateless
import com.shevelev.wizard_camera.shared.glide.GlideTarget
import com.shevelev.wizard_camera.shared.glide.clear
import com.shevelev.wizard_camera.shared.glide.loadCircle

class FiltersItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private var iconGlideTarget: GlideTarget? = null

    private val root by lazy { itemView.findViewById<FrameLayout>(R.id.root) }
    private val favoriteButton by lazy { itemView.findViewById<PulseImageButton>(R.id.favoriteButton) }
    private val settingsButton by lazy { itemView.findViewById<PulseImageButtonStateless>(R.id.settingsButton) }
    private val listItemIcon by lazy { itemView.findViewById<ImageView>(R.id.listItemIcon) }

    fun bind(item: FilterListItem, position: Int, eventsProcessor: FilterEventsProcessor) {
        iconGlideTarget = listItemIcon.loadCircle(item.displayData.icon)
        root.tag = FiltersItemTag(id = item.displayData.id, position = position)

        if(!item.isSelected) {
            favoriteButton.visibility = View.INVISIBLE
        } else {
            when(item.favorite) {
                FilterFavoriteType.FAVORITE -> {
                    favoriteButton.visibility = View.VISIBLE
                    favoriteButton.isActive = true
                }
                FilterFavoriteType.NOT_FAVORITE -> {
                    favoriteButton.visibility = View.VISIBLE
                    favoriteButton.isActive = false
                }
                FilterFavoriteType.HIDDEN -> {
                    favoriteButton.visibility = View.INVISIBLE
                }
            }
        }

        if(item.isSelected) {
            settingsButton.visibility = if (item.hasSettings) View.VISIBLE else View.INVISIBLE
        } else {
            settingsButton.visibility = View.INVISIBLE
        }

        favoriteButton.setOnPulseButtonClickListener { isActive ->
            eventsProcessor.onFavoriteFilterClick(item.displayData.id, isActive)
        }

        settingsButton.setOnPulseButtonClickListener {
            eventsProcessor.onSettingsClick(item.displayData.id)
        }
    }

    fun recycle() {
        iconGlideTarget?.clear(itemView.context.applicationContext)
        favoriteButton.setOnPulseButtonClickListener(null)
        settingsButton.setOnPulseButtonClickListener(null)
    }
}