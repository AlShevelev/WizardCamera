package com.shevelev.wizard_camera.main_activity.view.widgets.filters_settings

import android.content.Context
import android.util.AttributeSet
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.SwirlFilterSettings
import kotlinx.android.synthetic.main.view_filter_settings_swirl.view.*
import kotlin.random.Random

class FilterSettingsSwirl
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr),
    FilterSettingsWidget {

    private var onSettingsChangeListener: ((FilterSettings) -> Unit)? = null
    private lateinit var settings: SwirlFilterSettings

    private val minValue = 1
    private val maxValue = 10

    init {
        inflate(context, R.layout.view_filter_settings_swirl, this)
    }

    override val title: Int = R.string.filterSwirlSettings

    override fun init(startSettings: FilterSettings) {
        this.settings = startSettings as SwirlFilterSettings

        sizeBar.max = maxValue - minValue
        sizeBar.progress = settings.radius - minValue

        sizeBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                settings = settings.copy(radius = progress + minValue)
                onSettingsChangeListener?.invoke(settings)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        rotationBar.max = maxValue - minValue
        rotationBar.progress = settings.rotation - minValue

        rotationBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                settings = settings.copy(rotation = progress + minValue)
                onSettingsChangeListener?.invoke(settings)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        invertedRotation.isChecked = settings.invertRotation
        invertedRotation.setOnCheckedChangeListener { _, isChecked ->
            settings = settings.copy(invertRotation = isChecked)
            onSettingsChangeListener?.invoke(settings)
        }
    }

    override fun setOnSettingsChangeListener(listener: ((FilterSettings) -> Unit)?) {
        onSettingsChangeListener = listener
    }
}