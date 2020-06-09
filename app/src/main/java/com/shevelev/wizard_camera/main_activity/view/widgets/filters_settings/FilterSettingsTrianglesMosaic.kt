package com.shevelev.wizard_camera.main_activity.view.widgets.filters_settings

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.common_entities.enums.Size
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.TrianglesMosaicFilterSettings
import kotlinx.android.synthetic.main.view_filter_settings_block_size.view.*

class FilterSettingsTrianglesMosaic
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr),
    FilterSettingsWidget {

    private var onSettingsChangeListener: ((FilterSettings) -> Unit)? = null
    private lateinit var settings: TrianglesMosaicFilterSettings

    init {
        inflate(context, R.layout.view_filter_settings_block_size, this)
    }

    override val title: Int = R.string.filterTrianglesMosaicSettings

    override fun init(startSettings: FilterSettings) {
        titleText.setText(R.string.triangleSize)

        this.settings = startSettings as TrianglesMosaicFilterSettings

        when(startSettings.size) {
            Size.SMALL -> sizeGroup.check(R.id.smallSize)
            Size.NORMAL -> sizeGroup.check(R.id.normalSize)
            Size.LARGE -> sizeGroup.check(R.id.largeSize)
        }

        sizeGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.smallSize -> onSettingsChangeListener?.invoke(startSettings.copy(size = Size.SMALL))
                R.id.normalSize -> onSettingsChangeListener?.invoke(startSettings.copy(size = Size.NORMAL))
                R.id.largeSize -> onSettingsChangeListener?.invoke(startSettings.copy(size = Size.LARGE))
            }
        }
    }

    override fun setOnSettingsChangeListener(listener: ((FilterSettings) -> Unit)?) {
        onSettingsChangeListener = listener
    }
}