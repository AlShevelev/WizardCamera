package com.shevelev.wizard_camera.shared.filters_ui.filters_settings.system

import android.content.Context
import android.util.AttributeSet
import android.util.Range
import android.widget.FrameLayout
import android.widget.SeekBar
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.shared.filters_ui.filters_settings.FilterSettingsWidget
import com.shevelev.wizard_camera.common_entities.filter_settings.system.GeneralSystemFilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.system.SystemFilterSettings
import com.shevelev.wizard_camera.shared.ui_utils.setOnChangeListener
import com.shevelev.wizard_camera.utils.useful_ext.reduceToRange

abstract class SystemFilterSettingsBase
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    FilterSettingsWidget<SystemFilterSettings> {

    private var onSettingsChangeListener: ((SystemFilterSettings) -> Unit)? = null
    private lateinit var settings: GeneralSystemFilterSettings

    private val valueBar by lazy { findViewById<SeekBar>(R.id.valueBar) }

    /**
     * Min value for a seek bar
     */
    protected abstract val displayFactorMin: Float

    /**
     * Max value for a seek bar
     */
    protected abstract val displayFactorMax: Float

    /**
     * Min value for an effect settings
     */
    protected abstract val effectFactorMin: Float

    /**
     * Max value for an effect settings
     */
    protected abstract val effectFactorMax: Float

    init {
        @Suppress("LeakingThis")
        inflate(context, R.layout.view_filter_settings_single_bar, this)
    }

    override fun init(startSettings: SystemFilterSettings) {
        this.settings = startSettings as GeneralSystemFilterSettings

        valueBar.max = (displayFactorMax - displayFactorMin).toInt()
        valueBar.progress = settings.filterValue
            .reduceToRange(
                Range(effectFactorMin, effectFactorMax),
                Range(displayFactorMin, displayFactorMax))
            .toInt()

        valueBar.setOnChangeListener {
            val value = it.toFloat()
                .reduceToRange(Range(
                    displayFactorMin, displayFactorMax),
                    Range(effectFactorMin, effectFactorMax))

            settings = settings.copy(filterValue = value)
            onSettingsChangeListener?.invoke(settings)
        }
    }

    override fun setOnSettingsChangeListener(listener: ((SystemFilterSettings) -> Unit)?) {
        onSettingsChangeListener = listener
    }
}