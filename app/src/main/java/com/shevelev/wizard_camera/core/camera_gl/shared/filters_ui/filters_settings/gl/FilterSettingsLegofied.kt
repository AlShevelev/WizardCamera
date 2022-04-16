package com.shevelev.wizard_camera.core.camera_gl.shared.filters_ui.filters_settings.gl

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.core.camera_gl.shared.filters_ui.filters_settings.FilterSettingsWidget
import com.shevelev.wizard_camera.core.common_entities.enums.Size
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.LegofiedFilterSettings

class FilterSettingsLegofied
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr),
    FilterSettingsWidget<GlFilterSettings> {

    private var onSettingsChangeListener: ((GlFilterSettings) -> Unit)? = null
    private lateinit var settings: LegofiedFilterSettings

    private val titleText by lazy { findViewById<TextView>(R.id.titleText) }
    private val sizeGroup by lazy { findViewById<RadioGroup>(R.id.sizeGroup) }

    init {
        inflate(context, R.layout.view_filter_settings_block_size, this)
    }

    override val title: Int = R.string.filterLegofiedSettings

    override fun init(startSettings: GlFilterSettings) {
        titleText.setText(R.string.blockSize)

        this.settings = startSettings as LegofiedFilterSettings

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

    override fun setOnSettingsChangeListener(listener: ((GlFilterSettings) -> Unit)?) {
        onSettingsChangeListener = listener
    }
}