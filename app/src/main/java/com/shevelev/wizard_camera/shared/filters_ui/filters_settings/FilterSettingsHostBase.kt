package com.shevelev.wizard_camera.shared.filters_ui.filters_settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.core.common_entities.filter_settings.FilterSettings

/**
 * Base class for all filer setting hosts
 */
@Suppress("LeakingThis")
abstract class FilterSettingsHostBase<T: FilterSettings<*>>
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var onSettingsChangeListener: ((T) -> Unit)? = null

    private var settingsWidget: FilterSettingsWidget<T>? = null

    private val settingsContainer by lazy { findViewById<FrameLayout>(R.id.settingsContainer) }
    private val headerText by lazy { findViewById<TextView>(R.id.headerText) }

    init {
        inflate(context, R.layout.view_filter_settings_host, this)

        setOnClickListener {  } // To prevent unexpected close the "dialog"
    }

    fun setOnSettingsChangeListener(listener: ((T) -> Unit)?) {
        onSettingsChangeListener = listener
    }

    @Suppress("UNCHECKED_CAST")
    fun show(settings: T) {
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

    protected abstract fun createWidget(settings: T): FilterSettingsWidget<T>
}