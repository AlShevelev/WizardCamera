package com.shevelev.wizard_camera.filters.filters_settings.gl

import android.content.Context
import android.util.AttributeSet
import android.widget.CheckBox
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.filters.filters_settings.FilterSettingsWidget
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.SwirlFilterSettings
import com.shevelev.wizard_camera.core.ui_utils.ext.setOnChangeListener

class FilterSettingsSwirl
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr),
    FilterSettingsWidget {

    private var onSettingsChangeListener: ((GlFilterSettings) -> Unit)? = null
    private lateinit var settings: SwirlFilterSettings

    private val minValue = 1
    private val maxValue = 10

    private val sizeBar by lazy { findViewById<SeekBar>(R.id.sizeBar) }
    private val rotationBar by lazy { findViewById<SeekBar>(R.id.rotationBar) }
    private val invertedRotation by lazy { findViewById<CheckBox>(R.id.invertedRotation) }

    init {
        inflate(context, R.layout.view_filter_settings_swirl, this)
    }

    override val title: Int = R.string.filterSwirlSettings

    override fun init(startSettings: GlFilterSettings) {
        this.settings = startSettings as SwirlFilterSettings

        sizeBar.max = maxValue - minValue
        sizeBar.progress = settings.radius - minValue

        sizeBar.setOnChangeListener {
            settings = settings.copy(radius = it + minValue)
            onSettingsChangeListener?.invoke(settings)
        }

        rotationBar.max = maxValue - minValue
        rotationBar.progress = settings.rotation - minValue

        rotationBar.setOnChangeListener {
            settings = settings.copy(rotation = it + minValue)
            onSettingsChangeListener?.invoke(settings)
        }

        invertedRotation.isChecked = settings.invertRotation
        invertedRotation.setOnCheckedChangeListener { _, isChecked ->
            settings = settings.copy(invertRotation = isChecked)
            onSettingsChangeListener?.invoke(settings)
        }
    }

    override fun setOnSettingsChangeListener(listener: ((GlFilterSettings) -> Unit)?) {
        onSettingsChangeListener = listener
    }
}