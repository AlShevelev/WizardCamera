package com.shevelev.wizard_camera.main_activity.view.widgets.filters_settings

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.common_entities.filter_settings.BlackAndWhiteFilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import kotlinx.android.synthetic.main.view_filter_settings_single_check.view.*

class FilterSettingsBlackAndWhite
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    FilterSettingsWidget {

    private var onSettingsChangeListener: ((FilterSettings) -> Unit)? = null
    private lateinit var settings: FilterSettings

    init {
        inflate(context, R.layout.view_filter_settings_single_check, this)
    }

    override val title: Int = R.string.filterBlackAndWhiteSettings

    override fun init(startSettings: FilterSettings) {
        this.settings = startSettings

        check.isChecked = (startSettings as BlackAndWhiteFilterSettings).isInverted

        check.setOnCheckedChangeListener {
            _, isChecked -> onSettingsChangeListener?.invoke(startSettings.copy(isInverted = isChecked))
        }
    }

    override fun setOnSettingsChangeListener(listener: ((FilterSettings) -> Unit)?) {
        onSettingsChangeListener = listener
    }
}