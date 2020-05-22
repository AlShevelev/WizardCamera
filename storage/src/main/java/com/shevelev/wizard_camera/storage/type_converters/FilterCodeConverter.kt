package com.shevelev.wizard_camera.storage.type_converters

import androidx.room.TypeConverter
import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import java.lang.UnsupportedOperationException

class FilterCodeConverter {
    @TypeConverter
    fun toDb(sourceData: FilterCode?): Int? =
        // We shouldn't use enum value here - the transformation must be separated from the entity 
        sourceData?.let {
            when(it) {
                FilterCode.ORIGINAL -> 0
                FilterCode.EDGE_DETECTION -> 1 
                FilterCode.PIXELIZE -> 2
                FilterCode.EM_INTERFERENCE -> 3 
                FilterCode.TRIANGLES_MOSAIC -> 4
                FilterCode.LEGOFIED -> 5
                FilterCode.TILE_MOSAIC -> 6
                FilterCode.BLUE_ORANGE -> 7
                FilterCode.CHROMATIC_ABERRATION -> 8 
                FilterCode.BASIC_DEFORM -> 9
                FilterCode.CONTRAST -> 10
                FilterCode.NOISE_WARP -> 11
                FilterCode.REFRACTION -> 12
                FilterCode.MAPPING -> 13
                FilterCode.CROSSHATCH -> 14 
                FilterCode.LICHTENSTEIN_ESQUE -> 15 
                FilterCode.ASCII_ART -> 16
                FilterCode.MONEY -> 17
                FilterCode.CRACKED -> 18
                FilterCode.POLYGONIZATION -> 19 
                FilterCode.BLACK_AND_WHITE -> 20
                FilterCode.GRAY -> 21
                FilterCode.NEGATIVE -> 22
                FilterCode.NOSTALGIA -> 23
                FilterCode.CASTING -> 24
                FilterCode.RELIEF -> 25
                FilterCode.SWIRL -> 26
                FilterCode.HEXAGON_MOSAIC -> 27
                FilterCode.MIRROR -> 28
                FilterCode.TRIPLE -> 29
                FilterCode.CARTOON -> 30
                FilterCode.WATER_REFLECTION -> 31
            }           
        }

    @TypeConverter
    fun fromDb(sourceData: Int?): FilterCode? =
        sourceData.let { 
            when(it) {
                0 -> FilterCode.ORIGINAL
                1 -> FilterCode.EDGE_DETECTION
                2 -> FilterCode.PIXELIZE
                3 -> FilterCode.EM_INTERFERENCE
                4 -> FilterCode.TRIANGLES_MOSAIC
                5 -> FilterCode.LEGOFIED
                6 -> FilterCode.TILE_MOSAIC
                7 -> FilterCode.BLUE_ORANGE
                8 -> FilterCode.CHROMATIC_ABERRATION
                9 -> FilterCode.BASIC_DEFORM
                10 -> FilterCode.CONTRAST
                11 -> FilterCode.NOISE_WARP
                12 -> FilterCode.REFRACTION
                13 -> FilterCode.MAPPING
                14 -> FilterCode.CROSSHATCH
                15 -> FilterCode.LICHTENSTEIN_ESQUE
                16 -> FilterCode.ASCII_ART
                17 -> FilterCode.MONEY
                18 -> FilterCode.CRACKED
                19 -> FilterCode.POLYGONIZATION
                20 -> FilterCode.BLACK_AND_WHITE
                21 -> FilterCode.GRAY
                22 -> FilterCode.NEGATIVE
                23 -> FilterCode.NOSTALGIA
                24 -> FilterCode.CASTING
                25 -> FilterCode.RELIEF
                26 -> FilterCode.SWIRL
                27 -> FilterCode.HEXAGON_MOSAIC
                28 -> FilterCode.MIRROR
                29 -> FilterCode.TRIPLE
                30 -> FilterCode.CARTOON
                31 -> FilterCode.WATER_REFLECTION
                else -> throw UnsupportedOperationException("This value is not supported: $it")
            }
        }
}