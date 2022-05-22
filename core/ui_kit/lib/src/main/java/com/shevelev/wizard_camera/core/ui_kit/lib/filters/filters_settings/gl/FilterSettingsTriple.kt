package com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_settings.gl

import android.content.Context
import android.util.AttributeSet
import android.widget.CheckBox
import android.widget.FrameLayout
import com.shevelev.wizard_camera.core.ui_kit.lib.R
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.TripleFilterSettings
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_settings.FilterSettingsWidget

class FilterSettingsTriple
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    FilterSettingsWidget {

    private var onSettingsChangeListener: ((GlFilterSettings) -> Unit)? = null
    private lateinit var settings: GlFilterSettings

    private val check by lazy { findViewById<CheckBox>(R.id.check) }

    init {
        inflate(context, R.layout.view_filter_settings_single_check, this)
    }

    override val title: Int = R.string.filterTripleSettings

    override fun init(startSettings: GlFilterSettings) {
        this.settings = startSettings

        check.setText(R.string.horizontal)
        check.isChecked = (startSettings as TripleFilterSettings).isHorizontal

        check.setOnCheckedChangeListener {
            _, isChecked -> onSettingsChangeListener?.invoke(startSettings.copy(isHorizontal = isChecked))
        }
    }

    override fun setOnSettingsChangeListener(listener: ((GlFilterSettings) -> Unit)?) {
        onSettingsChangeListener = listener
    }
}