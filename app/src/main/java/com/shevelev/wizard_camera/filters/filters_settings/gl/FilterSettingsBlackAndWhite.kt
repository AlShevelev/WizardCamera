package com.shevelev.wizard_camera.filters.filters_settings.gl

import android.content.Context
import android.util.AttributeSet
import android.widget.CheckBox
import android.widget.FrameLayout
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.filters.filters_settings.FilterSettingsWidget
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.BlackAndWhiteFilterSettings
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings

class FilterSettingsBlackAndWhite
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    FilterSettingsWidget<GlFilterSettings> {

    private var onSettingsChangeListener: ((GlFilterSettings) -> Unit)? = null
    private lateinit var settings: GlFilterSettings

    private val check by lazy { findViewById<CheckBox>(R.id.check) }

    init {
        inflate(context, R.layout.view_filter_settings_single_check, this)
    }

    override val title: Int = R.string.filterBlackAndWhiteSettings

    override fun init(startSettings: GlFilterSettings) {
        this.settings = startSettings

        check.isChecked = (startSettings as BlackAndWhiteFilterSettings).isInverted

        check.setOnCheckedChangeListener {
            _, isChecked -> onSettingsChangeListener?.invoke(startSettings.copy(isInverted = isChecked))
        }
    }

    override fun setOnSettingsChangeListener(listener: ((GlFilterSettings) -> Unit)?) {
        onSettingsChangeListener = listener
    }
}