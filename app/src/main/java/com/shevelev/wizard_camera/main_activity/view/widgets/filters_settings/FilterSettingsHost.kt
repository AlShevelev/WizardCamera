package com.shevelev.wizard_camera.main_activity.view.widgets.filters_settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import kotlinx.android.synthetic.main.view_filter_settings_host.view.*
import java.lang.UnsupportedOperationException

class FilterSettingsHost
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var onSettingsChangeListener: ((FilterSettings) -> Unit)? = null

    private var settingsWidget: View? = null

    init {
        inflate(context, R.layout.view_filter_settings_host, this)

        setOnClickListener {  } // To prevent unexpected close the "dialog"
    }

    fun setOnSettingsChangeListener(listener: ((FilterSettings) -> Unit)?) {
        onSettingsChangeListener = listener
    }

    fun show(settings: FilterSettings) {
        if(visibility == View.VISIBLE) {
            return
        }

        settingsWidget = createWidget(settings)

        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        settingsContainer.addView(settingsWidget, layoutParams)

        with(settingsWidget as FilterSettingsWidget) {
            headerText.setText(title)
            init(settings)
            setOnSettingsChangeListener(onSettingsChangeListener)
        }

        visibility = View.VISIBLE
    }

    fun hide() {
        if(visibility != View.VISIBLE) {
            return
        }

        visibility = View.INVISIBLE

        settingsContainer.removeView(settingsWidget)

        (settingsWidget as? FilterSettingsWidget)?.setOnSettingsChangeListener(null)
        settingsWidget = null
    }

    private fun createWidget(settings: FilterSettings): View =
        when(settings.code) {
            FilterCode.EDGE_DETECTION -> FilterSettingsEdgeDetection(context)
            FilterCode.BLACK_AND_WHITE -> FilterSettingsBlackAndWhite(context)
            FilterCode.LEGOFIED -> FilterSettingsLegofied(context)
            FilterCode.TRIANGLES_MOSAIC -> FilterSettingsTrianglesMosaic(context)
            FilterCode.HEXAGON_MOSAIC -> FilterSettingsHexagonMosaic(context)
            FilterCode.CRACKED -> FilterSettingsCracked(context)
            FilterCode.SWIRL -> FilterSettingsSwirl(context)
            FilterCode.TILE_MOSAIC -> FilterSettingsTileMosaic(context)
            FilterCode.TRIPLE -> FilterSettingsTriple(context)
            else -> throw UnsupportedOperationException("This code is not supported: ${settings.code}")
        }
}