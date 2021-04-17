package com.shevelev.wizard_camera.filter.factory

import android.content.Context
import com.shevelev.wizard_camera.camera.R
import com.shevelev.wizard_camera.filter.*
import com.shevelev.wizard_camera.common_entities.enums.FilterCode

class FiltersFactory(private val context: Context) {
    private val filters = mutableMapOf<FilterCode, CameraFilter>()

    fun getFilter(code: FilterCode): CameraFilter = filters[code] ?: createFilter(code).apply { filters[code] = this }

    private fun createFilter(code: FilterCode): CameraFilter =
        when(code) {
            FilterCode.ORIGINAL -> CameraFilter(context, R.raw.original)
            FilterCode.EDGE_DETECTION -> EdgeDetectionCameraFilter(context)
            FilterCode.PIXELIZE -> CameraFilter(context, R.raw.pixelize)
            FilterCode.EM_INTERFERENCE -> CameraFilter(context, R.raw.em_interference)
            FilterCode.TRIANGLES_MOSAIC -> TrianglesMosaicCameraFilter(context)
            FilterCode.LEGOFIED -> LegofiedCameraFilter(context)
            FilterCode.TILE_MOSAIC -> TileMosaicCameraFilter(context)
            FilterCode.BLUE_ORANGE -> CameraFilter(context, R.raw.blue_orange)
            FilterCode.CHROMATIC_ABERRATION -> CameraFilter(context, R.raw.chromatic_aberration)
            FilterCode.BASIC_DEFORM -> CameraFilter(context, R.raw.basic_deform)
            FilterCode.CONTRAST -> CameraFilter(context, R.raw.contrast)
            FilterCode.NOISE_WARP -> CameraFilter(context, R.raw.noise_warp)
            FilterCode.REFRACTION -> RefractionCameraFilter(context)
            FilterCode.MAPPING -> MappingCameraFilter(context)
            FilterCode.CROSSHATCH -> CameraFilter(context, R.raw.crosshatch)
            FilterCode.NEWSPAPER -> NewspaperCameraFilter(context)
            FilterCode.ASCII_ART -> CameraFilter(context, R.raw.ascii_art)
            FilterCode.MONEY -> CameraFilter(context, R.raw.money_filter)
            FilterCode.CRACKED -> CrackedCameraFilter(context)
            FilterCode.POLYGONIZATION -> CameraFilter(context, R.raw.polygonization)
            FilterCode.BLACK_AND_WHITE -> BlackAndWhiteCameraFilter(context)
            FilterCode.GRAY -> CameraFilter(context, R.raw.gray)
            FilterCode.NEGATIVE -> CameraFilter(context, R.raw.negative)
            FilterCode.NOSTALGIA -> CameraFilter(context, R.raw.nostalgia)
            FilterCode.CASTING -> CameraFilter(context, R.raw.casting)
            FilterCode.RELIEF -> CameraFilter(context, R.raw.relief)
            FilterCode.SWIRL -> SwirlCameraFilter(context)
            FilterCode.HEXAGON_MOSAIC -> HexagonMosaicCameraFilter(context)
            FilterCode.MIRROR -> CameraFilter(context, R.raw.mirror)
            FilterCode.TRIPLE -> TripleCameraFilter(context)
            FilterCode.CARTOON -> CameraFilter(context, R.raw.cartoon)
            FilterCode.WATER_REFLECTION -> CameraFilter(context, R.raw.water_reflection)
        }
}