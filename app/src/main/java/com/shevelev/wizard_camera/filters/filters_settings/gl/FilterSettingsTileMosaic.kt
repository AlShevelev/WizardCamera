package com.shevelev.wizard_camera.filters.filters_settings.gl

import android.content.Context
import android.util.AttributeSet
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.filters.filters_settings.FilterSettingsWidget
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.TileMosaicFilterSettings
import com.shevelev.wizard_camera.core.ui_utils.ext.setOnChangeListener

class FilterSettingsTileMosaic
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr),
    FilterSettingsWidget {

    private var onSettingsChangeListener: ((GlFilterSettings) -> Unit)? = null
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

    override fun init(startSettings: GlFilterSettings) {
        this.settings = startSettings as TileMosaicFilterSettings

        tileSizeBar.max = maxTileSizeValue - minTileSizeValue
        tileSizeBar.progress = settings.tileSize - minTileSizeValue

        tileSizeBar.setOnChangeListener {
            settings = settings.copy(tileSize = it + minTileSizeValue)
            onSettingsChangeListener?.invoke(settings)
        }

        borderSizeBar.max = maxBorderSizeValue - minBorderSizeValue
        borderSizeBar.progress = settings.borderSize - minBorderSizeValue

        borderSizeBar.setOnChangeListener {
            settings = settings.copy(borderSize = it + minBorderSizeValue)
            onSettingsChangeListener?.invoke(settings)
        }
    }

    override fun setOnSettingsChangeListener(listener: ((GlFilterSettings) -> Unit)?) {
        onSettingsChangeListener = listener
    }
}