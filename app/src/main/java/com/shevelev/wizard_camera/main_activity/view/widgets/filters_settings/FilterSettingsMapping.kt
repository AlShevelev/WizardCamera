package com.shevelev.wizard_camera.main_activity.view.widgets.filters_settings

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.common_entities.enums.MappingFilterTexture
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.MappingFilterSettings
import kotlinx.android.synthetic.main.view_filter_settings_mapping.view.*

@Suppress("MapGetWithNotNullAssertionOperator")
class FilterSettingsMapping
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr),
    FilterSettingsWidget {

    private var onSettingsChangeListener: ((FilterSettings) -> Unit)? = null
    private lateinit var settings: MappingFilterSettings

    private val minMixFactor = 5
    private val maxMixFactor = 20

    private val texturesToWidget: Map<MappingFilterTexture, View>

    private lateinit var selectedTextureWidget: View

    init {
        inflate(context, R.layout.view_filter_settings_mapping, this)

        texturesToWidget = mapOf(
            MappingFilterTexture.TEXTURE_0 to mappingTex0,
            MappingFilterTexture.TEXTURE_1 to mappingTex1,
            MappingFilterTexture.TEXTURE_2 to mappingTex2,
            MappingFilterTexture.TEXTURE_3 to mappingTex3,
            MappingFilterTexture.TEXTURE_4 to mappingTex4,
            MappingFilterTexture.TEXTURE_5 to mappingTex5,
            MappingFilterTexture.TEXTURE_6 to mappingTex6,
            MappingFilterTexture.TEXTURE_7 to mappingTex7,
            MappingFilterTexture.TEXTURE_8 to mappingTex8,
            MappingFilterTexture.TEXTURE_9 to mappingTex9,
            MappingFilterTexture.TEXTURE_10 to mappingTex10,
            MappingFilterTexture.TEXTURE_11 to mappingTex11,
            MappingFilterTexture.TEXTURE_12 to mappingTex12,
            MappingFilterTexture.TEXTURE_13 to mappingTex13,
            MappingFilterTexture.TEXTURE_14 to  mappingTex14
        )
    }

    override val title: Int = R.string.filterMappingSettings

    override fun init(startSettings: FilterSettings) {
        this.settings = startSettings as MappingFilterSettings

        mixFactorBar.max = maxMixFactor - minMixFactor
        mixFactorBar.progress = settings.mixFactor - minMixFactor

        mixFactorBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                settings = settings.copy(mixFactor = progress + minMixFactor)
                onSettingsChangeListener?.invoke(settings)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        selectedTextureWidget = texturesToWidget[settings.texture]!!
        setSelected(selectedTextureWidget)

        MappingFilterTexture.values().forEach { texture ->
            texturesToWidget[texture]!!.setOnClickListener { onTextureWidgetClick(texture) }
        }

        scrollListTo(settings.texture)
    }

    override fun setOnSettingsChangeListener(listener: ((FilterSettings) -> Unit)?) {
        onSettingsChangeListener = listener
    }

    private fun onTextureWidgetClick(texture: MappingFilterTexture) {
        val newTextureWidget = texturesToWidget[texture]!!

        if(newTextureWidget == selectedTextureWidget) {
            return
        }

        setUnselected(selectedTextureWidget)
        setSelected(newTextureWidget)

        selectedTextureWidget = newTextureWidget

        settings = settings.copy(texture = texture)
        onSettingsChangeListener?.invoke(settings)
    }

    private fun setSelected(textureWidget: View) {
        textureWidget.setBackgroundColor(Color.WHITE)

        val padding = context.resources.getDimension(R.dimen.strokeWidthNormal).toInt()+1
        textureWidget.setPadding(padding, padding,  padding, padding)
    }

    private fun setUnselected(textureWidget: View) {
        textureWidget.setPadding(0, 0, 0, 0)
    }

    private fun scrollListTo(texture: MappingFilterTexture) {
        texturesList.post {
            texturesList.scrollTo( texturesToWidget[texture]!!.left, 0)
        }
    }
}