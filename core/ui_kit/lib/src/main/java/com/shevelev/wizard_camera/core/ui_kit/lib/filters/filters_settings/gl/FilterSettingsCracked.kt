package com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_settings.gl

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.shevelev.wizard_camera.core.ui_kit.lib.R
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.CrackedFilterSettings
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.ui_kit.lib.filters.filters_settings.FilterSettingsWidget
import com.shevelev.wizard_camera.core.ui_utils.ext.setOnChangeListener
import kotlin.random.Random

class FilterSettingsCracked
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr),
    FilterSettingsWidget {

    private var onSettingsChangeListener: ((GlFilterSettings) -> Unit)? = null
    private lateinit var settings: CrackedFilterSettings

    private val minShards = 3
    private val maxShards = 30

    private val totalShards by lazy { findViewById<SeekBar>(R.id.totalShards) }
    private val randomShardsButton by lazy { findViewById<Button>(R.id.randomShardsButton) }

    init {
        inflate(context, R.layout.view_filter_settings_cracked, this)
    }

    override val title: Int = R.string.filterCrackedSettings

    override fun init(startSettings: GlFilterSettings) {
        this.settings = startSettings as CrackedFilterSettings

        totalShards.max = maxShards - minShards
        totalShards.progress = settings.shards - minShards

        totalShards.setOnChangeListener {
            settings = settings.copy(shards = it + minShards)
            onSettingsChangeListener?.invoke(settings)
        }

        randomShardsButton.setOnClickListener {
            settings = settings.copy(
                randomA = Random.nextDouble(1.0, 359.0).toFloat(),
                randomB = Random.nextDouble(1.0, 359.0).toFloat(),
                randomC = Random.nextDouble(1.0, 359.0).toFloat())
            onSettingsChangeListener?.invoke(settings)
        }
    }

    override fun setOnSettingsChangeListener(listener: ((GlFilterSettings) -> Unit)?) {
        onSettingsChangeListener = listener
    }
}