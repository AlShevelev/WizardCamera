package com.shevelev.wizard_camera.activity_main.fragment_camera.view.widgets.filters_settings

import android.content.Context
import android.util.AttributeSet
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.TileMosaicFilterSettings

class FilterSettingsTileMosaic
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr),
    FilterSettingsWidget {

    private var onSettingsChangeListener: ((FilterSettings) -> Unit)? = null
    private lateinit var settings: TileMosaicFilterSettings

    private val minTileSizeValue = 40
    private val maxTileSizeValue = 100

    private val minBorderSizeValue = 1
    private val maxBorderSizeValue = 5

    private val tileSizeBar by lazy { findViewById<SeekBar>(R.id.tileSizeBar) }
    private val borderSizeBar by lazy { findViewById<SeekBar>(R.id.borderSizeBar) }

    init {
        inflate(context, R.layout.view_filter_settings_tile_mosaic, this)
    }

    override val title: Int = R.string.filterTileMosaicSettings

    override fun init(startSettings: FilterSettings) {
        this.settings = startSettings as TileMosaicFilterSettings

        tileSizeBar.max = maxTileSizeValue - minTileSizeValue
        tileSizeBar.progress = settings.tileSize - minTileSizeValue

        tileSizeBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                settings = settings.copy(tileSize = progress + minTileSizeValue)
                onSettingsChangeListener?.invoke(settings)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        borderSizeBar.max = maxBorderSizeValue - minBorderSizeValue
        borderSizeBar.progress = settings.borderSize - minBorderSizeValue

        borderSizeBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                settings = settings.copy(borderSize = progress + minBorderSizeValue)
                onSettingsChangeListener?.invoke(settings)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    override fun setOnSettingsChangeListener(listener: ((FilterSettings) -> Unit)?) {
        onSettingsChangeListener = listener
    }
}