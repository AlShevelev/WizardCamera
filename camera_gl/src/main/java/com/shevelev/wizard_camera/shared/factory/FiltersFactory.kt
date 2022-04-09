package com.shevelev.wizard_camera.shared.factory

import android.content.Context
import androidx.annotation.RawRes
import com.shevelev.wizard_camera.camera.R
import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.*
import com.shevelev.wizard_camera.shared.*

/**
 * Creates filters
 */
object FiltersFactory {
    /**
     * Create filter's resourceId by it's code
     */
    @RawRes
    fun getFilterRes(code: GlFilterCode): Int =
        when(code) {
            GlFilterCode.ORIGINAL -> R.raw.original
            GlFilterCode.EDGE_DETECTION -> R.raw.edge_detection
            GlFilterCode.PIXELIZE -> R.raw.pixelize
            GlFilterCode.TRIANGLES_MOSAIC -> R.raw.triangles_mosaic
            GlFilterCode.LEGOFIED -> R.raw.legofied
            GlFilterCode.TILE_MOSAIC -> R.raw.tile_mosaic
            GlFilterCode.BLUE_ORANGE -> R.raw.blue_orange
            GlFilterCode.BASIC_DEFORM -> R.raw.basic_deform
            GlFilterCode.CONTRAST -> R.raw.contrast
            GlFilterCode.NOISE_WARP -> R.raw.noise_warp
            GlFilterCode.REFRACTION -> R.raw.refraction
            GlFilterCode.MAPPING -> R.raw.mapping
            GlFilterCode.CROSSHATCH -> R.raw.crosshatch
            GlFilterCode.NEWSPAPER -> R.raw.newspaper
            GlFilterCode.ASCII_ART -> R.raw.ascii_art
            GlFilterCode.MONEY -> R.raw.money_filter
            GlFilterCode.CRACKED -> R.raw.cracked
            GlFilterCode.POLYGONIZATION -> R.raw.polygonization
            GlFilterCode.BLACK_AND_WHITE -> R.raw.black_and_white
            GlFilterCode.GRAY -> R.raw.gray
            GlFilterCode.NEGATIVE -> R.raw.negative
            GlFilterCode.NOSTALGIA -> R.raw.nostalgia
            GlFilterCode.CASTING -> R.raw.casting
            GlFilterCode.RELIEF -> R.raw.relief
            GlFilterCode.SWIRL -> R.raw.swirl
            GlFilterCode.HEXAGON_MOSAIC -> R.raw.hexagon_mosaic
            GlFilterCode.MIRROR -> R.raw.mirror
            GlFilterCode.TRIPLE -> R.raw.triple
            GlFilterCode.CARTOON -> R.raw.cartoon
            GlFilterCode.WATER_REFLECTION -> R.raw.water_reflection
        }

    /**
     * Create an OGL wrapper around a filter based on its settings
     */
    fun createGLFilterSettings(settings: GlFilterSettings, context: Context) : GLFilterSettings =
        when(settings.code) {
            GlFilterCode.ORIGINAL -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            GlFilterCode.EDGE_DETECTION -> EdgeDetectionGLFilterSettings(settings as EdgeDetectionFilterSettings)
            GlFilterCode.PIXELIZE -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            GlFilterCode.TRIANGLES_MOSAIC -> TrianglesMosaicGLFilterSettings(settings as TrianglesMosaicFilterSettings)
            GlFilterCode.LEGOFIED -> LegofiedGLFilterSettings(settings as LegofiedFilterSettings)
            GlFilterCode.TILE_MOSAIC -> TileMosaicGLFilterSettings(settings as TileMosaicFilterSettings)
            GlFilterCode.BLUE_ORANGE -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            GlFilterCode.BASIC_DEFORM -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            GlFilterCode.CONTRAST -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            GlFilterCode.NOISE_WARP -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            GlFilterCode.REFRACTION -> RefractionGLFilterSettings(context, settings as EmptyFilterSettings)
            GlFilterCode.MAPPING -> MappingGLFilterSettings(context, settings as MappingFilterSettings)
            GlFilterCode.CROSSHATCH -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            GlFilterCode.NEWSPAPER -> NewspaperGLFilterSettings(settings as NewspaperFilterSettings)
            GlFilterCode.ASCII_ART -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            GlFilterCode.MONEY -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            GlFilterCode.CRACKED -> CrackedGLFilterSettings(settings as CrackedFilterSettings)
            GlFilterCode.POLYGONIZATION -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            GlFilterCode.BLACK_AND_WHITE -> BlackAndWhiteGLFilterSettings(settings as BlackAndWhiteFilterSettings)
            GlFilterCode.GRAY -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            GlFilterCode.NEGATIVE -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            GlFilterCode.NOSTALGIA -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            GlFilterCode.CASTING -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            GlFilterCode.RELIEF -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            GlFilterCode.SWIRL -> SwirlGLFilterSettings(settings as SwirlFilterSettings)
            GlFilterCode.HEXAGON_MOSAIC -> HexagonMosaicGLFilterSettings(settings as HexagonMosaicFilterSettings)
            GlFilterCode.MIRROR -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            GlFilterCode.TRIPLE -> TripleGLFilterSettings(settings as TripleFilterSettings)
            GlFilterCode.CARTOON -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
            GlFilterCode.WATER_REFLECTION -> EmptyGLFilterSettings(settings as EmptyFilterSettings)
        }
}