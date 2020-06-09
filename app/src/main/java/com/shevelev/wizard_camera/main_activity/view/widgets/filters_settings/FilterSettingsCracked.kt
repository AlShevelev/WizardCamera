package com.shevelev.wizard_camera.main_activity.view.widgets.filters_settings

import android.content.Context
import android.util.AttributeSet
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.common_entities.filter_settings.CrackedFilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import kotlinx.android.synthetic.main.view_filter_settings_cracked.view.*
import kotlin.random.Random

class FilterSettingsCracked
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr),
    FilterSettingsWidget {

    private var onSettingsChangeListener: ((FilterSettings) -> Unit)? = null
    private lateinit var settings: CrackedFilterSettings

    private val minShards = 3
    private val maxShards = 30

    init {
        inflate(context, R.layout.view_filter_settings_cracked, this)
    }

    override val title: Int = R.string.filterCrackedSettings

    override fun init(startSettings: FilterSettings) {
        this.settings = startSettings as CrackedFilterSettings

        totalShards.max = maxShards - minShards
        totalShards.progress = settings.shards - minShards

        totalShards.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                settings = settings.copy(shards = progress + minShards)
                onSettingsChangeListener?.invoke(settings)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        randomShardsButton.setOnClickListener {
            settings = settings.copy(
                randomA = Random.nextDouble(1.0, 359.0).toFloat(),
                randomB = Random.nextDouble(1.0, 359.0).toFloat(),
                randomC = Random.nextDouble(1.0, 359.0).toFloat())
            onSettingsChangeListener?.invoke(settings)
        }
    }

    override fun setOnSettingsChangeListener(listener: ((FilterSettings) -> Unit)?) {
        onSettingsChangeListener = listener
    }
}