package com.shevelev.wizard_camera.core.camera_gl.shared.filters_ui.filters_settings.gl

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.core.camera_gl.shared.filters_ui.filters_settings.FilterSettingsWidget
import com.shevelev.wizard_camera.core.common_entities.enums.MappingFilterTexture
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.MappingFilterSettings

@Suppress("MapGetWithNotNullAssertionOperator")
class FilterSettingsMapping
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr),
    FilterSettingsWidget<GlFilterSettings> {

    private var onSettingsChangeListener: ((GlFilterSettings) -> Unit)? = null
    private lateinit var settings: MappingFilterSettings

    private val minMixFactor = 5
    private val maxMixFactor = 20

    private val texturesToWidget: Map<MappingFilterTexture, View>

    private lateinit var selectedTextureWidget: View

    private val mappingTex0 by lazy { findViewById<ImageView>(R.id.mappingTex0) }
    private val mappingTex1 by lazy { findViewById<ImageView>(R.id.mappingTex1) }
    private val mappingTex2 by lazy { findViewById<ImageView>(R.id.mappingTex2) }
    private val mappingTex3 by lazy { findViewById<ImageView>(R.id.mappingTex3) }
    private val mappingTex4 by lazy { findViewById<ImageView>(R.id.mappingTex4) }
    private val mappingTex5 by lazy { findViewById<ImageView>(R.id.mappingTex5) }
    private val mappingTex6 by lazy { findViewById<ImageView>(R.id.mappingTex6) }
    private val mappingTex7 by lazy { findViewById<ImageView>(R.id.mappingTex7) }
    private val mappingTex8 by lazy { findViewById<ImageView>(R.id.mappingTex8) }
    private val mappingTex9 by lazy { findViewById<ImageView>(R.id.mappingTex9) }
    private val mappingTex10 by lazy { findViewById<ImageView>(R.id.mappingTex10) }
    private val mappingTex11 by lazy { findViewById<ImageView>(R.id.mappingTex11) }
    private val mappingTex12 by lazy { findViewById<ImageView>(R.id.mappingTex12) }
    private val mappingTex13 by lazy { findViewById<ImageView>(R.id.mappingTex13) }
    private val mappingTex14 by lazy { findViewById<ImageView>(R.id.mappingTex14) }

    private val mixFactorBar by lazy { findViewById<SeekBar>(R.id.mixFactorBar) }
    private val texturesList by lazy { findViewById<HorizontalScrollView>(R.id.texturesList) }

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

    override fun init(startSettings: GlFilterSettings) {
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

    override fun setOnSettingsChangeListener(listener: ((GlFilterSettings) -> Unit)?) {
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