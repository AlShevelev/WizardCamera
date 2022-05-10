package com.shevelev.wizard_camera.filters.filters_settings.gl.host

import android.content.Context
import android.util.AttributeSet
import com.shevelev.wizard_camera.filters.filters_settings.FilterSettingsHostBase
import com.shevelev.wizard_camera.filters.filters_settings.FilterSettingsWidget
import com.shevelev.wizard_camera.filters.filters_settings.gl.*
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings

class GlFilterSettingsHost
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FilterSettingsHostBase(context, attrs, defStyleAttr)  {

    override fun createWidget(settings: GlFilterSettings): FilterSettingsWidget =
        when(settings.code) {
            GlFilterCode.EDGE_DETECTION -> FilterSettingsEdgeDetection(context)
            GlFilterCode.BLACK_AND_WHITE -> FilterSettingsBlackAndWhite(context)
            GlFilterCode.LEGOFIED -> FilterSettingsLegofied(context)
            GlFilterCode.TRIANGLES_MOSAIC -> FilterSettingsTrianglesMosaic(context)
            GlFilterCode.HEXAGON_MOSAIC -> FilterSettingsHexagonMosaic(context)
            GlFilterCode.CRACKED -> FilterSettingsCracked(context)
            GlFilterCode.SWIRL -> FilterSettingsSwirl(context)
            GlFilterCode.TILE_MOSAIC -> FilterSettingsTileMosaic(context)
            GlFilterCode.TRIPLE -> FilterSettingsTriple(context)
            GlFilterCode.NEWSPAPER -> FilterSettingsNewspaper(context)
            GlFilterCode.MAPPING -> FilterSettingsMapping(context)
            else -> throw UnsupportedOperationException("This code is not supported: ${settings.code}")
        }
}