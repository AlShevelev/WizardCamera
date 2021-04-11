package com.shevelev.wizard_camera.storage.type_converters.filter_settings

import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.*
import com.squareup.moshi.Moshi
import javax.inject.Inject

class FilerSettingsConverterImpl 
@Inject
constructor(
    private val moshi: Moshi
) : FilerSettingsConverter {
    override fun toString(settings: FilterSettings): String =
        when (settings.code) {
            FilterCode.EDGE_DETECTION -> toJson(EdgeDetectionFilterSettings::class.java, settings)
            FilterCode.BLACK_AND_WHITE -> toJson(BlackAndWhiteFilterSettings::class.java, settings)
            FilterCode.LEGOFIED -> toJson(LegofiedFilterSettings::class.java, settings)
            FilterCode.TRIANGLES_MOSAIC -> toJson(TrianglesMosaicFilterSettings::class.java, settings)
            FilterCode.HEXAGON_MOSAIC -> toJson(HexagonMosaicFilterSettings::class.java, settings)
            FilterCode.CRACKED -> toJson(CrackedFilterSettings::class.java, settings)
            FilterCode.SWIRL -> toJson(SwirlFilterSettings::class.java, settings)
            FilterCode.TILE_MOSAIC -> toJson(TileMosaicFilterSettings::class.java, settings)
            FilterCode.TRIPLE -> toJson(TripleFilterSettings::class.java, settings)
            FilterCode.NEWSPAPER -> toJson(NewspaperFilterSettings::class.java, settings)
            FilterCode.MAPPING -> toJson(MappingFilterSettings::class.java, settings)
            else -> toJson(EmptyFilterSettings::class.java, settings)
        }

    override fun fromString(code: FilterCode, settings: String): FilterSettings =
        when (code) {
            FilterCode.EDGE_DETECTION -> fromJson(EdgeDetectionFilterSettings::class.java, settings)
            FilterCode.BLACK_AND_WHITE -> fromJson(BlackAndWhiteFilterSettings::class.java, settings)
            FilterCode.LEGOFIED -> fromJson(LegofiedFilterSettings::class.java, settings)
            FilterCode.TRIANGLES_MOSAIC -> fromJson(TrianglesMosaicFilterSettings::class.java, settings)
            FilterCode.HEXAGON_MOSAIC -> fromJson(HexagonMosaicFilterSettings::class.java, settings)
            FilterCode.CRACKED -> fromJson(CrackedFilterSettings::class.java, settings)
            FilterCode.SWIRL -> fromJson(SwirlFilterSettings::class.java, settings)
            FilterCode.TILE_MOSAIC -> fromJson(TileMosaicFilterSettings::class.java, settings)
            FilterCode.TRIPLE -> fromJson(TripleFilterSettings::class.java, settings)
            FilterCode.NEWSPAPER -> fromJson(NewspaperFilterSettings::class.java, settings)
            FilterCode.MAPPING -> fromJson(MappingFilterSettings::class.java, settings)
            else -> fromJson(EmptyFilterSettings::class.java, settings)
        }

    private fun <T>fromJson(cls: Class<T>, settings: String) = moshi.adapter(cls).fromJson(settings)!!

    @Suppress("UNCHECKED_CAST")
    private fun <T>toJson(cls: Class<T>, settings: FilterSettings) = moshi.adapter(cls).toJson(settings as T)
}