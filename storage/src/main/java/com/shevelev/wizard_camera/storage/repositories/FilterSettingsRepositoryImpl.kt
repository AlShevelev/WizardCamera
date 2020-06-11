package com.shevelev.wizard_camera.storage.repositories

import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.*
import com.shevelev.wizard_camera.storage.core.DbCore
import com.shevelev.wizard_camera.storage.entities.FilterSettingsDb
import com.shevelev.wizard_camera.utils.id.IdUtil
import com.squareup.moshi.Moshi
import javax.inject.Inject

class FilterSettingsRepositoryImpl
@Inject
constructor(
    private val db: DbCore,
    private val moshi: Moshi
) : FilterSettingsRepository {
    override fun read(): List<FilterSettings> = db.filterSettings.read().map { it.map() }

    override fun update(settings: FilterSettings) {
        db.runConsistent {
            val dbRecord = db.filterSettings.read(settings.code)

            if(dbRecord == null) {
                db.filterSettings.create(settings.map(IdUtil.generateLongId()))
            } else {
                db.filterSettings.update(settings.map(dbRecord.id))
            }
        }
    }

    private fun FilterSettingsDb.map(): FilterSettings =
        when(code) {
            FilterCode.EDGE_DETECTION -> fromJson(EdgeDetectionFilterSettings::class.java, settings)
            FilterCode.BLACK_AND_WHITE -> fromJson(BlackAndWhiteFilterSettings::class.java, settings)
            FilterCode.LEGOFIED -> fromJson(LegofiedFilterSettings::class.java, settings)
            FilterCode.TRIANGLES_MOSAIC -> fromJson(TrianglesMosaicFilterSettings::class.java, settings)
            FilterCode.HEXAGON_MOSAIC -> fromJson(HexagonMosaicFilterSettings::class.java, settings)
            FilterCode.CRACKED -> fromJson(CrackedFilterSettings::class.java, settings)
            FilterCode.SWIRL -> fromJson(SwirlFilterSettings::class.java, settings)
            FilterCode.TILE_MOSAIC -> fromJson(TileMosaicFilterSettings::class.java, settings)
            FilterCode.TRIPLE -> fromJson(TripleFilterSettings::class.java, settings)
            else -> throw UnsupportedOperationException("This code is not supported: ${this.code}")
        }

    private fun FilterSettings.map(id: Long): FilterSettingsDb {
        val settingsStr= when (code) {
            FilterCode.EDGE_DETECTION -> toJson(EdgeDetectionFilterSettings::class.java, this)
            FilterCode.BLACK_AND_WHITE -> toJson(BlackAndWhiteFilterSettings::class.java, this)
            FilterCode.LEGOFIED -> toJson(LegofiedFilterSettings::class.java, this)
            FilterCode.TRIANGLES_MOSAIC -> toJson(TrianglesMosaicFilterSettings::class.java, this)
            FilterCode.HEXAGON_MOSAIC -> toJson(HexagonMosaicFilterSettings::class.java, this)
            FilterCode.CRACKED -> toJson(CrackedFilterSettings::class.java, this)
            FilterCode.SWIRL -> toJson(SwirlFilterSettings::class.java, this)
            FilterCode.TILE_MOSAIC -> toJson(TileMosaicFilterSettings::class.java, this)
            FilterCode.TRIPLE -> toJson(TripleFilterSettings::class.java, this)
            else -> throw UnsupportedOperationException("This code is not supported: ${this.code}")
        }

        return FilterSettingsDb(id, code, settingsStr)
    }

    private fun <T>fromJson(cls: Class<T>, settings: String) = moshi.adapter(cls).fromJson(settings)!!

    @Suppress("UNCHECKED_CAST")
    private fun <T>toJson(cls: Class<T>, settings: FilterSettings) = moshi.adapter(cls).toJson(settings as T)
}