package com.shevelev.wizard_camera.gl

import android.content.Context
import androidx.annotation.IdRes
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.gl.filter.CameraFilter
import com.shevelev.wizard_camera.gl.filter.MappingCameraFilter
import com.shevelev.wizard_camera.gl.filter.RefractionCameraFilter

class FiltersFactory(context: Context) {
    private val filters = mutableMapOf(         // All filters must be created in a renderer thread
        R.id.filterOriginal to CameraFilter(context, R.raw.original),
        R.id.filterEdgeDectection to CameraFilter(context, R.raw.edge_detection),
        R.id.filterPixelize to CameraFilter(context, R.raw.pixelize),
        R.id.filterEMInterference to CameraFilter(context, R.raw.em_interference),
        R.id.filterTrianglesMosaic to CameraFilter(context, R.raw.triangles_mosaic),
        R.id.filterLegofied to CameraFilter(context, R.raw.legofied),
        R.id.filterTileMosaic to CameraFilter(context, R.raw.tile_mosaic),
        R.id.filterBlueorange to CameraFilter(context, R.raw.blue_orange),
        R.id.filterChromaticAberration to CameraFilter(context, R.raw.chromatic_aberration),
        R.id.filterBasicDeform to CameraFilter(context, R.raw.basic_deform),
        R.id.filterContrast to CameraFilter(context, R.raw.contrast),
        R.id.filterNoiseWarp to CameraFilter(context, R.raw.noise_warp),
        R.id.filterRefraction to RefractionCameraFilter(context),
        R.id.filterMapping to MappingCameraFilter(context),
        R.id.filterCrosshatch to CameraFilter(context, R.raw.crosshatch),
        R.id.filterLichtensteinEsque to CameraFilter(context, R.raw.lichtenstein_esque),
        R.id.filterAsciiArt to CameraFilter(context, R.raw.ascii_art),
        R.id.filterMoney to CameraFilter(context, R.raw.money_filter),
        R.id.filterCracked to CameraFilter(context, R.raw.cracked),
        R.id.filterPolygonization to CameraFilter(context, R.raw.polygonization),
        R.id.filterBlackAndWhite to CameraFilter(context, R.raw.black_and_white),
        R.id.filterGray to CameraFilter(context, R.raw.gray),
        R.id.filterNegative to CameraFilter(context, R.raw.negative),
        R.id.filterNostalgia to CameraFilter(context, R.raw.nostalgia),
        R.id.filterCasting to CameraFilter(context, R.raw.casting),
        R.id.filterRelief to CameraFilter(context, R.raw.relief),
        R.id.filterSwirl to CameraFilter(context, R.raw.swirl),
        R.id.filterHexagonMosaic to CameraFilter(context, R.raw.hexagon_mosaic),
        R.id.filterMirror to CameraFilter(context, R.raw.mirror),
        R.id.filterTriple to CameraFilter(context, R.raw.triple),
        R.id.filterCartoon to CameraFilter(context, R.raw.cartoon),
        R.id.filterWaterReflection to CameraFilter(context, R.raw.water_reflection)
    )

    fun getFilter(@IdRes key: Int): CameraFilter =
        filters[key] ?: throw Exception("This key is not supported: $key")
}