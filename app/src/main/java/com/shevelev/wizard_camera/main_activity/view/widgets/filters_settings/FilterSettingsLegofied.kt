package com.shevelev.wizard_camera.main_activity.view.widgets.filters_settings

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.common_entities.enums.Size
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.LegofiedFilterSettings
import kotlinx.android.synthetic.main.view_filters_settings_legofied.view.*

class FilterSettingsLegofied
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr),
    FilterSettingsWidget {

    private var onSettingsChangeListener: ((FilterSettings) -> Unit)? = null
    private lateinit var settings: LegofiedFilterSettings

    init {
        inflate(context, R.layout.view_filters_settings_legofied, this)
    }

    override val title: Int = R.string.filterBlackAndWhiteSettings

    override fun init(settings: FilterSettings) {
        this.settings = settings as LegofiedFilterSettings

        when(settings.size) {
            Size.SMALL -> sizeGroup.check(R.id.smallSize)
            Size.NORMAL -> sizeGroup.check(R.id.normalSize)
            Size.LARGE -> sizeGroup.check(R.id.largeSize)
        }

        sizeGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.smallSize -> onSettingsChangeListener?.invoke(settings.copy(size = Size.SMALL))
                R.id.normalSize -> onSettingsChangeListener?.invoke(settings.copy(size = Size.NORMAL))
                R.id.largeSize -> onSettingsChangeListener?.invoke(settings.copy(size = Size.LARGE))
            }
        }
    }

    override fun setOnSettingsChangeListener(listener: ((FilterSettings) -> Unit)?) {
        onSettingsChangeListener = listener
    }
}