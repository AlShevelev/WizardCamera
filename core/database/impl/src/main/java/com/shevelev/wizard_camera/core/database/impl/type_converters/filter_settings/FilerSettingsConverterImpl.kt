package com.shevelev.wizard_camera.core.database.impl.type_converters.filter_settings

import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.*
import com.squareup.moshi.Moshi

internal class FilerSettingsConverterImpl(
    private val moshi: Moshi
) : FilerSettingsConverter {
    override fun toString(settings: GlFilterSettings): String =
        when (settings.code) {
            GlFilterCode.EDGE_DETECTION -> toJson(EdgeDetectionFilterSettings::class.java, settings)
            GlFilterCode.BLACK_AND_WHITE -> toJson(BlackAndWhiteFilterSettings::class.java, settings)
            GlFilterCode.LEGOFIED -> toJson(LegofiedFilterSettings::class.java, settings)
            GlFilterCode.TRIANGLES_MOSAIC -> toJson(TrianglesMosaicFilterSettings::class.java, settings)
            GlFilterCode.HEXAGON_MOSAIC -> toJson(HexagonMosaicFilterSettings::class.java, settings)
            GlFilterCode.CRACKED -> toJson(CrackedFilterSettings::class.java, settings)
            GlFilterCode.SWIRL -> toJson(SwirlFilterSettings::class.java, settings)
            GlFilterCode.TILE_MOSAIC -> toJson(TileMosaicFilterSettings::class.java, settings)
            GlFilterCode.TRIPLE -> toJson(TripleFilterSettings::class.java, settings)
            GlFilterCode.NEWSPAPER -> toJson(NewspaperFilterSettings::class.java, settings)
            GlFilterCode.MAPPING -> toJson(MappingFilterSettings::class.java, settings)
            else -> toJson(EmptyFilterSettings::class.java, settings)
        }

    override fun fromString(code: GlFilterCode, settings: String): GlFilterSettings =
        when (code) {
            GlFilterCode.EDGE_DETECTION -> fromJson(EdgeDetectionFilterSettings::class.java, settings)
            GlFilterCode.BLACK_AND_WHITE -> fromJson(BlackAndWhiteFilterSettings::class.java, settings)
            GlFilterCode.LEGOFIED -> fromJson(LegofiedFilterSettings::class.java, settings)
            GlFilterCode.TRIANGLES_MOSAIC -> fromJson(TrianglesMosaicFilterSettings::class.java, settings)
            GlFilterCode.HEXAGON_MOSAIC -> fromJson(HexagonMosaicFilterSettings::class.java, settings)
            GlFilterCode.CRACKED -> fromJson(CrackedFilterSettings::class.java, settings)
            GlFilterCode.SWIRL -> fromJson(SwirlFilterSettings::class.java, settings)
            GlFilterCode.TILE_MOSAIC -> fromJson(TileMosaicFilterSettings::class.java, settings)
            GlFilterCode.TRIPLE -> fromJson(TripleFilterSettings::class.java, settings)
            GlFilterCode.NEWSPAPER -> fromJson(NewspaperFilterSettings::class.java, settings)
            GlFilterCode.MAPPING -> fromJson(MappingFilterSettings::class.java, settings)
            else -> fromJson(EmptyFilterSettings::class.java, settings)
        }

    private fun <T>fromJson(cls: Class<T>, settings: String) = moshi.adapter(cls).fromJson(settings)!!

    @Suppress("UNCHECKED_CAST")
    private fun <T>toJson(cls: Class<T>, settings: GlFilterSettings) = moshi.adapter(cls).toJson(settings as T)
}