package com.shevelev.wizard_camera.main_activity.view.widgets.filters_settings

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.common_entities.filter_settings.EdgeDetectionFilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import kotlinx.android.synthetic.main.view_filters_settings_edge_detection.view.*

class FilterSettingsEdgeDetection
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    FilterSettingsWidget {

    private var onSettingsChangeListener: ((FilterSettings) -> Unit)? = null
    private lateinit var settings: FilterSettings

    init {
        inflate(context, R.layout.view_filters_settings_edge_detection, this)
    }

    override val title: Int = R.string.filterEdgeDetectionSettings

    override fun init(settings: FilterSettings) {
        this.settings = settings

        inverted.isChecked = (settings as EdgeDetectionFilterSettings).isInverted

        inverted.setOnCheckedChangeListener {
            _, isChecked -> onSettingsChangeListener?.invoke(settings.copy(isInverted = isChecked))
        }
    }

    override fun setOnSettingsChangeListener(listener: ((FilterSettings) -> Unit)?) {
        onSettingsChangeListener = listener
    }
}