package com.shevelev.wizard_camera.camera.filter.factory

import android.content.Context
import com.shevelev.wizard_camera.camera.R
import com.shevelev.wizard_camera.camera.filter.*
import com.shevelev.wizard_camera.common_entities.enums.FilterCode

class FiltersFactory(context: Context) {
    private val filters = mutableMapOf(         // All filters must be created in a renderer thread
        FilterCode.ORIGINAL to CameraFilter(context, R.raw.original),
        FilterCode.EDGE_DETECTION to EdgeDetectionCameraFilter(context),
        FilterCode.PIXELIZE to CameraFilter(context, R.raw.pixelize),
        FilterCode.EM_INTERFERENCE to CameraFilter(context, R.raw.em_interference),
        FilterCode.TRIANGLES_MOSAIC to TrianglesMosaicCameraFilter(context),
        FilterCode.LEGOFIED to LegofiedCameraFilter(context),
        FilterCode.TILE_MOSAIC to TileMosaicCameraFilter(context),
        FilterCode.BLUE_ORANGE to CameraFilter(context, R.raw.blue_orange),
        FilterCode.CHROMATIC_ABERRATION to CameraFilter(context, R.raw.chromatic_aberration),
        FilterCode.BASIC_DEFORM to CameraFilter(context, R.raw.basic_deform),
        FilterCode.CONTRAST to CameraFilter(context, R.raw.contrast),
        FilterCode.NOISE_WARP to CameraFilter(context, R.raw.noise_warp),
        FilterCode.REFRACTION to RefractionCameraFilter(context),
        FilterCode.MAPPING to MappingCameraFilter(context),
        FilterCode.CROSSHATCH to CameraFilter(context, R.raw.crosshatch),
        FilterCode.NEWSPAPER to NewspaperCameraFilter(context),
        FilterCode.ASCII_ART to CameraFilter(context, R.raw.ascii_art),
        FilterCode.MONEY to CameraFilter(context, R.raw.money_filter),
        FilterCode.CRACKED to CrackedCameraFilter(context),
        FilterCode.POLYGONIZATION to CameraFilter(context, R.raw.polygonization),
        FilterCode.BLACK_AND_WHITE to BlackAndWhiteCameraFilter(context),
        FilterCode.GRAY to CameraFilter(context, R.raw.gray),
        FilterCode.NEGATIVE to CameraFilter(context, R.raw.negative),
        FilterCode.NOSTALGIA to CameraFilter(context, R.raw.nostalgia),
        FilterCode.CASTING to CameraFilter(context, R.raw.casting),
        FilterCode.RELIEF to CameraFilter(context, R.raw.relief),
        FilterCode.SWIRL to SwirlCameraFilter(context),
        FilterCode.HEXAGON_MOSAIC to HexagonMosaicCameraFilter(context),
        FilterCode.MIRROR to CameraFilter(context, R.raw.mirror),
        FilterCode.TRIPLE to TripleCameraFilter(context),
        FilterCode.CARTOON to CameraFilter(context, R.raw.cartoon),
        FilterCode.WATER_REFLECTION to CameraFilter(context, R.raw.water_reflection)
    )

    fun getFilter(key: FilterCode): CameraFilter = filters[key] ?: throw Exception("This key is not supported: $key")
}