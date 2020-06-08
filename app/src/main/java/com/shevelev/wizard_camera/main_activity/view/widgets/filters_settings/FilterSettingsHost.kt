package com.shevelev.wizard_camera.main_activity.view.widgets.filters_settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import kotlinx.android.synthetic.main.view_filters_settings_host.view.*
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
        inflate(context, R.layout.view_filters_settings_host, this)
    }

    fun setOnSettingsChangeListener(listener: ((FilterSettings) -> Unit)?) {
        onSettingsChangeListener = listener
    }

    fun show(settings: FilterSettings) {
        settingsWidget = createWidget(settings)

        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        settingsContainer.addView(settingsWidget, layoutParams)

        with(settingsWidget as FilterSettingsWidget) {
            headerText.setText(title)
            init(settings)
            setOnSettingsChangeListener(onSettingsChangeListener)
        }

        visibility = View.VISIBLE
    }

    fun hide() {
        visibility = View.INVISIBLE

        settingsContainer.removeView(settingsWidget)

        (settingsWidget as? FilterSettingsWidget)?.setOnSettingsChangeListener(null)
        settingsWidget = null
    }

    private fun createWidget(settings: FilterSettings): View =
        when(settings.code) {
            FilterCode.EDGE_DETECTION -> FilterSettingsEdgeDetection(context)
            else -> throw UnsupportedOperationException("This code is not supported: ${settings.code}")
        }
}