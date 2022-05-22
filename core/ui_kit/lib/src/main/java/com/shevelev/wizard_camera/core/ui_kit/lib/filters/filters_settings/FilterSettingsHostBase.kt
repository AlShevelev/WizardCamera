package com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.shevelev.wizard_camera.core.ui_kit.lib.R
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings

/**
 * Base class for all filer setting hosts
 */
@Suppress("LeakingThis")
abstract class FilterSettingsHostBase
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var onSettingsChangeListener: ((GlFilterSettings) -> Unit)? = null

    private var settingsWidget: FilterSettingsWidget? = null

    private val settingsContainer by lazy { findViewById<FrameLayout>(R.id.settingsContainer) }
    private val headerText by lazy { findViewById<TextView>(R.id.headerText) }

    init {
        inflate(context, R.layout.view_filter_settings_host, this)

        setOnClickListener {  } // To prevent unexpected close the "dialog"
    }

    fun setOnSettingsChangeListener(listener: ((GlFilterSettings) -> Unit)?) {
        onSettingsChangeListener = listener
    }

    @Suppress("UNCHECKED_CAST")
    fun show(settings: GlFilterSettings) {
        if(visibility == View.VISIBLE) {
            return
        }

        settingsWidget = createWidget(settings)

        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        settingsContainer.addView(settingsWidget as View, layoutParams)

        settingsWidget?.let {
            headerText.setText(it.title)
            it.init(settings)
            it.setOnSettingsChangeListener(onSettingsChangeListener)
        }

        visibility = View.VISIBLE
    }

    @Suppress("UNCHECKED_CAST")
    fun hide() {
        if(visibility != View.VISIBLE) {
            return
        }

        visibility = View.INVISIBLE

        settingsContainer.removeView(settingsWidget as View)

        settingsWidget?.setOnSettingsChangeListener(null)
        settingsWidget = null
    }

    protected abstract fun createWidget(settings: GlFilterSettings): FilterSettingsWidget
}