package com.shevelev.wizard_camera.camera

import android.content.Context
import com.shevelev.wizard_camera.camera.filter.CameraFilter
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.camera.filter.MappingCameraFilter
import com.shevelev.wizard_camera.camera.filter.RefractionCameraFilter

class FiltersFactory(context: Context) {
    private val filters = mutableMapOf(         // All filters must be created in a renderer thread
        FilterCode.ORIGINAL to CameraFilter(context, R.raw.original),
        FilterCode.EDGE_DETECTION to CameraFilter(context, R.raw.edge_detection),
        FilterCode.PIXELIZE to CameraFilter(context, R.raw.pixelize),
        FilterCode.EM_INTERFERENCE to CameraFilter(context, R.raw.em_interference),
        FilterCode.TRIANGLES_MOSAIC to CameraFilter(context, R.raw.triangles_mosaic),
        FilterCode.LEGOFIED to CameraFilter(context, R.raw.legofied),
        FilterCode.TILE_MOSAIC to CameraFilter(context, R.raw.tile_mosaic),
        FilterCode.BLUE_ORANGE to CameraFilter(context, R.raw.blue_orange),
        FilterCode.CHROMATIC_ABERRATION to CameraFilter(context, R.raw.chromatic_aberration),
        FilterCode.BASIC_DEFORM to CameraFilter(context, R.raw.basic_deform),
        FilterCode.CONTRAST to CameraFilter(context, R.raw.contrast),
        FilterCode.NOISE_WARP to CameraFilter(context, R.raw.noise_warp),
        FilterCode.REFRACTION to RefractionCameraFilter(context),
        FilterCode.MAPPING to MappingCameraFilter(context),
        FilterCode.CROSSHATCH to CameraFilter(context, R.raw.crosshatch),
        FilterCode.LICHTENSTEIN_ESQUE to CameraFilter(context, R.raw.lichtenstein_esque),
        FilterCode.ASCII_ART to CameraFilter(context, R.raw.ascii_art),
        FilterCode.MONEY to CameraFilter(context, R.raw.money_filter),
        FilterCode.CRACKED to CameraFilter(context, R.raw.cracked),
        FilterCode.POLYGONIZATION to CameraFilter(context, R.raw.polygonization),
        FilterCode.BLACK_AND_WHITE to CameraFilter(context, R.raw.black_and_white),
        FilterCode.GRAY to CameraFilter(context, R.raw.gray),
        FilterCode.NEGATIVE to CameraFilter(context, R.raw.negative),
        FilterCode.NOSTALGIA to CameraFilter(context, R.raw.nostalgia),
        FilterCode.CASTING to CameraFilter(context, R.raw.casting),
        FilterCode.RELIEF to CameraFilter(context, R.raw.relief),
        FilterCode.SWIRL to CameraFilter(context, R.raw.swirl),
        FilterCode.HEXAGON_MOSAIC to CameraFilter(context, R.raw.hexagon_mosaic),
        FilterCode.MIRROR to CameraFilter(context, R.raw.mirror),
        FilterCode.TRIPLE to CameraFilter(context, R.raw.triple),
        FilterCode.CARTOON to CameraFilter(context, R.raw.cartoon),
        FilterCode.WATER_REFLECTION to CameraFilter(context, R.raw.water_reflection)
    )

    fun getFilter(key: FilterCode): CameraFilter = filters[key] ?: throw Exception("This key is not supported: $key")
}