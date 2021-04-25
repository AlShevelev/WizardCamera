package com.shevelev.wizard_camera.shared.factory

import android.content.Context
import androidx.annotation.RawRes
import com.shevelev.wizard_camera.camera.R
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.*
import com.shevelev.wizard_camera.shared.*

/**
 * Creates filters
 */
object FiltersFactory {
    /**
     * Create filter's resourceId by it's code
     */
    @RawRes
    fun getFilterRes(code: FilterCode): Int =
        when(code) {
            FilterCode.ORIGINAL -> R.raw.original
            FilterCode.EDGE_DETECTION -> R.raw.edge_detection
            FilterCode.PIXELIZE -> R.raw.pixelize
            FilterCode.TRIANGLES_MOSAIC -> R.raw.triangles_mosaic
            FilterCode.LEGOFIED -> R.raw.legofied
            FilterCode.TILE_MOSAIC -> R.raw.tile_mosaic
            FilterCode.BLUE_ORANGE -> R.raw.blue_orange
            FilterCode.BASIC_DEFORM -> R.raw.basic_deform
            FilterCode.CONTRAST -> R.raw.contrast
            FilterCode.NOISE_WARP -> R.raw.noise_warp
            FilterCode.REFRACTION -> R.raw.refraction
            FilterCode.MAPPING -> R.raw.mapping
            FilterCode.CROSSHATCH -> R.raw.crosshatch
            FilterCode.NEWSPAPER -> R.raw.newspaper
            FilterCode.ASCII_ART -> R.raw.ascii_art
            FilterCode.MONEY -> R.raw.money_filter
            FilterCode.CRACKED -> R.raw.cracked
            FilterCode.POLYGONIZATION -> R.raw.polygonization
            FilterCode.BLACK_AND_WHITE -> R.raw.black_and_white
            FilterCode.GRAY -> R.raw.gray
            FilterCode.NEGATIVE -> R.raw.negative
            FilterCode.NOSTALGIA -> R.raw.nostalgia
            FilterCode.CASTING -> R.raw.casting
            FilterCode.RELIEF -> R.raw.relief
            FilterCode.SWIRL -> R.raw.swirl
            FilterCode.HEXAGON_MOSAIC -> R.raw.hexagon_mosaic
            FilterCode.MIRROR -> R.raw.mirror
            FilterCode.TRIPLE -> R.raw.triple
            FilterCode.CARTOON -> R.raw.cartoon
            FilterCode.WATER_REFLECTION -> R.raw.water_reflection
        }

    /**
     * Create an OGL wrapper around a filter based on its settings
     */
    fun createGLFilterSettings(settings: FilterSettings, context: Context) : GLFilterSettings =
        when(settings.code) {
            FilterCode.ORIGINAL -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            FilterCode.EDGE_DETECTION -> EdgeDetectionGLFilterSettings(settings as EdgeDetectionFilterSettings)
            FilterCode.PIXELIZE -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            FilterCode.TRIANGLES_MOSAIC -> TrianglesMosaicGLFilterSettings(settings as TrianglesMosaicFilterSettings)
            FilterCode.LEGOFIED -> LegofiedGLFilterSettings(settings as LegofiedFilterSettings)
            FilterCode.TILE_MOSAIC -> TileMosaicGLFilterSettings(settings as TileMosaicFilterSettings)
            FilterCode.BLUE_ORANGE -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            FilterCode.BASIC_DEFORM -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            FilterCode.CONTRAST -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            FilterCode.NOISE_WARP -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            FilterCode.REFRACTION -> RefractionGLFilterSettings(context, settings as EmptyFilterSettings)
            FilterCode.MAPPING -> MappingGLFilterSettings(context, settings as MappingFilterSettings)
            FilterCode.CROSSHATCH -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            FilterCode.NEWSPAPER -> NewspaperGLFilterSettings(settings as NewspaperFilterSettings)
            FilterCode.ASCII_ART -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            FilterCode.MONEY -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            FilterCode.CRACKED -> CrackedGLFilterSettings(settings as CrackedFilterSettings)
            FilterCode.POLYGONIZATION -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            FilterCode.BLACK_AND_WHITE -> BlackAndWhiteGLFilterSettings(settings as BlackAndWhiteFilterSettings)
            FilterCode.GRAY -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            FilterCode.NEGATIVE -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            FilterCode.NOSTALGIA -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            FilterCode.CASTING -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            FilterCode.RELIEF -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            FilterCode.SWIRL -> SwirlGLFilterSettings(settings as SwirlFilterSettings)
            FilterCode.HEXAGON_MOSAIC -> HexagonMosaicGLFilterSettings(settings as HexagonMosaicFilterSettings)
            FilterCode.MIRROR -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            FilterCode.TRIPLE -> TripleGLFilterSettings(settings as TripleFilterSettings)
            FilterCode.CARTOON -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            FilterCode.WATER_REFLECTION -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
        }
}