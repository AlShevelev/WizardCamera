package com.shevelev.wizard_camera.common_entities.filter_settings.parceler

import android.os.Parcel
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.*
import kotlinx.android.parcel.Parceler

object FilterSettingsParceler : Parceler<FilterSettings> {
    /**
     * Reads the [FilterSettings] instance state from the [parcel], constructs the new [FilterSettings] instance and returns it.
     */
    @Suppress("MoveVariableDeclarationIntoWhen")
    override fun create(parcel: Parcel): FilterSettings {
        val codeValue = parcel.readInt()
        val code = FilterCode.values().first { it.value == codeValue }

        return when(code) {
            FilterCode.EDGE_DETECTION ->
                parcel.readParcelable<EdgeDetectionFilterSettings>(EdgeDetectionFilterSettings::class.java.classLoader)

            FilterCode.BLACK_AND_WHITE ->
                parcel.readParcelable<BlackAndWhiteFilterSettings>(BlackAndWhiteFilterSettings::class.java.classLoader)

            FilterCode.LEGOFIED ->
                parcel.readParcelable<LegofiedFilterSettings>(LegofiedFilterSettings::class.java.classLoader)

            FilterCode.TRIANGLES_MOSAIC ->
                parcel.readParcelable<TrianglesMosaicFilterSettings>(TrianglesMosaicFilterSettings::class.java.classLoader)

            FilterCode.HEXAGON_MOSAIC ->
                parcel.readParcelable<HexagonMosaicFilterSettings>(HexagonMosaicFilterSettings::class.java.classLoader)

            FilterCode.CRACKED ->
                parcel.readParcelable<CrackedFilterSettings>(CrackedFilterSettings::class.java.classLoader)

            FilterCode.SWIRL ->
                parcel.readParcelable<SwirlFilterSettings>(SwirlFilterSettings::class.java.classLoader)

            FilterCode.TILE_MOSAIC ->
                parcel.readParcelable<TileMosaicFilterSettings>(TileMosaicFilterSettings::class.java.classLoader)

            FilterCode.TRIPLE ->
                parcel.readParcelable<TripleFilterSettings>(TripleFilterSettings::class.java.classLoader)

            FilterCode.NEWSPAPER ->
                parcel.readParcelable<NewspaperFilterSettings>(NewspaperFilterSettings::class.java.classLoader)

            FilterCode.MAPPING ->
                parcel.readParcelable<MappingFilterSettings>(MappingFilterSettings::class.java.classLoader)

            else -> EmptyFilterSettings(code)
        } as FilterSettings
    }

    /**
     * Writes the [FilterSettings] instance state to the [parcel].
     */
    override fun FilterSettings.write(parcel: Parcel, flags: Int) {
        parcel.writeInt(code.value)

        when(code) {
            FilterCode.EDGE_DETECTION -> parcel.writeParcelable(this as EdgeDetectionFilterSettings, flags)
            FilterCode.BLACK_AND_WHITE -> parcel.writeParcelable(this as BlackAndWhiteFilterSettings, flags)
            FilterCode.LEGOFIED -> parcel.writeParcelable(this as LegofiedFilterSettings, flags)
            FilterCode.TRIANGLES_MOSAIC -> parcel.writeParcelable(this as TrianglesMosaicFilterSettings, flags)
            FilterCode.HEXAGON_MOSAIC -> parcel.writeParcelable(this as HexagonMosaicFilterSettings, flags)
            FilterCode.CRACKED -> parcel.writeParcelable(this as CrackedFilterSettings, flags)
            FilterCode.SWIRL -> parcel.writeParcelable(this as SwirlFilterSettings, flags)
            FilterCode.TILE_MOSAIC -> parcel.writeParcelable(this as TileMosaicFilterSettings, flags)
            FilterCode.TRIPLE -> parcel.writeParcelable(this as TripleFilterSettings, flags)
            FilterCode.NEWSPAPER -> parcel.writeParcelable(this as NewspaperFilterSettings, flags)
            FilterCode.MAPPING -> parcel.writeParcelable(this as MappingFilterSettings, flags)
            
            else -> {  }    // Do nothing
        }
    }
}