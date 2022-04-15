package com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.parceler

import android.os.Parcel
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.*
import kotlinx.parcelize.Parceler

object GlFilterSettingsParceler : Parceler<GlFilterSettings> {
    /**
     * Reads the [GlFilterSettings] instance state from the [parcel], constructs the new [GlFilterSettings] instance and returns it.
     */
    @Suppress("MoveVariableDeclarationIntoWhen")
    override fun create(parcel: Parcel): GlFilterSettings {
        val codeValue = parcel.readInt()
        val code = GlFilterCode.values().first { it.value == codeValue }

        return when(code) {
            GlFilterCode.EDGE_DETECTION ->
                parcel.readParcelable<EdgeDetectionFilterSettings>(EdgeDetectionFilterSettings::class.java.classLoader)

            GlFilterCode.BLACK_AND_WHITE ->
                parcel.readParcelable<BlackAndWhiteFilterSettings>(BlackAndWhiteFilterSettings::class.java.classLoader)

            GlFilterCode.LEGOFIED ->
                parcel.readParcelable<LegofiedFilterSettings>(LegofiedFilterSettings::class.java.classLoader)

            GlFilterCode.TRIANGLES_MOSAIC ->
                parcel.readParcelable<TrianglesMosaicFilterSettings>(TrianglesMosaicFilterSettings::class.java.classLoader)

            GlFilterCode.HEXAGON_MOSAIC ->
                parcel.readParcelable<HexagonMosaicFilterSettings>(HexagonMosaicFilterSettings::class.java.classLoader)

            GlFilterCode.CRACKED ->
                parcel.readParcelable<CrackedFilterSettings>(CrackedFilterSettings::class.java.classLoader)

            GlFilterCode.SWIRL ->
                parcel.readParcelable<SwirlFilterSettings>(SwirlFilterSettings::class.java.classLoader)

            GlFilterCode.TILE_MOSAIC ->
                parcel.readParcelable<TileMosaicFilterSettings>(TileMosaicFilterSettings::class.java.classLoader)

            GlFilterCode.TRIPLE ->
                parcel.readParcelable<TripleFilterSettings>(TripleFilterSettings::class.java.classLoader)

            GlFilterCode.NEWSPAPER ->
                parcel.readParcelable<NewspaperFilterSettings>(NewspaperFilterSettings::class.java.classLoader)

            GlFilterCode.MAPPING ->
                parcel.readParcelable<MappingFilterSettings>(MappingFilterSettings::class.java.classLoader)

            else -> EmptyFilterSettings(code)
        } as GlFilterSettings
    }

    /**
     * Writes the [GlFilterSettings] instance state to the [parcel].
     */
    override fun GlFilterSettings.write(parcel: Parcel, flags: Int) {
        parcel.writeInt(code.value)

        when(code) {
            GlFilterCode.EDGE_DETECTION -> parcel.writeParcelable(this as EdgeDetectionFilterSettings, flags)
            GlFilterCode.BLACK_AND_WHITE -> parcel.writeParcelable(this as BlackAndWhiteFilterSettings, flags)
            GlFilterCode.LEGOFIED -> parcel.writeParcelable(this as LegofiedFilterSettings, flags)
            GlFilterCode.TRIANGLES_MOSAIC -> parcel.writeParcelable(this as TrianglesMosaicFilterSettings, flags)
            GlFilterCode.HEXAGON_MOSAIC -> parcel.writeParcelable(this as HexagonMosaicFilterSettings, flags)
            GlFilterCode.CRACKED -> parcel.writeParcelable(this as CrackedFilterSettings, flags)
            GlFilterCode.SWIRL -> parcel.writeParcelable(this as SwirlFilterSettings, flags)
            GlFilterCode.TILE_MOSAIC -> parcel.writeParcelable(this as TileMosaicFilterSettings, flags)
            GlFilterCode.TRIPLE -> parcel.writeParcelable(this as TripleFilterSettings, flags)
            GlFilterCode.NEWSPAPER -> parcel.writeParcelable(this as NewspaperFilterSettings, flags)
            GlFilterCode.MAPPING -> parcel.writeParcelable(this as MappingFilterSettings, flags)
            
            else -> {  }    // Do nothing
        }
    }
}