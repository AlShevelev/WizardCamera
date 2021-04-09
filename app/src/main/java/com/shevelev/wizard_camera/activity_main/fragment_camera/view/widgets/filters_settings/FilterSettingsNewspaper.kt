package com.shevelev.wizard_camera.activity_main.fragment_camera.view.widgets.filters_settings

import android.content.Context
import android.util.AttributeSet
import android.widget.CheckBox
import android.widget.FrameLayout
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.NewspaperFilterSettings

class FilterSettingsNewspaper
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    FilterSettingsWidget {

    private var onSettingsChangeListener: ((FilterSettings) -> Unit)? = null
    private lateinit var settings: FilterSettings

    private val check by lazy { findViewById<CheckBox>(R.id.check) }

    init {
        inflate(context, R.layout.view_filter_settings_single_check, this)
    }

    override val title: Int = R.string.filterNewspaperSettings

    override fun init(startSettings: FilterSettings) {
        this.settings = startSettings

        check.setText(R.string.grayscale)
        check.isChecked = (startSettings as NewspaperFilterSettings).isGrayscale

        check.setOnCheckedChangeListener {
            _, isChecked -> onSettingsChangeListener?.invoke(startSettings.copy(isGrayscale = isChecked))
        }
    }

    override fun setOnSettingsChangeListener(listener: ((FilterSettings) -> Unit)?) {
        onSettingsChangeListener = listener
    }
}