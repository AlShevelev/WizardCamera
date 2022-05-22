package com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_carousel

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.shevelev.wizard_camera.core.ui_kit.lib.R
import com.shevelev.wizard_camera.core.ui_kit.lib.buttons.PulseImageButton
import com.shevelev.wizard_camera.core.ui_kit.lib.buttons.PulseImageButtonStateless
import com.shevelev.wizard_camera.core.ui_utils.glide.GlideTarget
import com.shevelev.wizard_camera.core.ui_utils.glide.clear
import com.shevelev.wizard_camera.core.ui_utils.glide.loadCircle

class FiltersItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private var iconGlideTarget: GlideTarget? = null

    private val root by lazy { itemView.findViewById<FrameLayout>(R.id.root) }
    private val favoriteButton by lazy { itemView.findViewById<PulseImageButton>(R.id.favoriteButton) }
    private val settingsButton by lazy { itemView.findViewById<PulseImageButtonStateless>(R.id.settingsButton) }
    private val listItemIcon by lazy { itemView.findViewById<ImageView>(R.id.listItemIcon) }

    fun bind(item: FilterListItem, position: Int, eventsProcessor: FilterEventsProcessor) {
        iconGlideTarget = listItemIcon.loadCircle(item.displayData.icon)
        root.tag = FiltersItemTag(id = item.displayData.code, position = position)

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

        settingsButton.visibility = if (item.hasSettings && item.isSelected) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }

        setSelection(
            icon = listItemIcon,
            context = root.context,
            isSelected = item.isSelected
        )

        favoriteButton.setOnPulseButtonClickListener { isActive ->
            eventsProcessor.onFavoriteFilterClick(item.displayData.code, isActive)
        }

        settingsButton.setOnPulseButtonClickListener {
            eventsProcessor.onSettingsClick(item.displayData.code)
        }

        root.setOnClickListener {
            eventsProcessor.onFilterClick(item.displayData.code, item.listId)
        }
    }

    fun recycle() {
        iconGlideTarget?.clear(itemView.context.applicationContext)

        favoriteButton.setOnPulseButtonClickListener(null)
        settingsButton.setOnPulseButtonClickListener(null)
        root.setOnClickListener(null)
    }

    private fun setSelection(icon: View, context: Context, isSelected: Boolean) {
        val frameThickness = if(isSelected) R.dimen.strokeWidthSelected else R.dimen.strokeWidthThin
        val padding = context.resources.getDimension(frameThickness).toInt()+1
        icon.setPadding(padding, padding, padding, padding)

        if(isSelected) {
            icon.setBackgroundResource(R.drawable.bcg_circle_yellow)
        } else {
            icon.setBackgroundResource(R.drawable.bcg_circle_white)
        }
    }
}