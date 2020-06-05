package com.shevelev.wizard_camera.storage.repositories

import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.EdgeDetectionFilterSettings
import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
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
            FilterCode.EDGE_DETECTION -> moshi.adapter(EdgeDetectionFilterSettings::class.java).fromJson(settings)!!
            else -> throw UnsupportedOperationException("This code is not supported: ${this.code}")
        }

    private fun FilterSettings.map(id: Long): FilterSettingsDb {
        val settingsStr= when (code) {
            FilterCode.EDGE_DETECTION ->
                moshi.adapter(EdgeDetectionFilterSettings::class.java).toJson(this as EdgeDetectionFilterSettings)
            else -> throw UnsupportedOperationException("This code is not supported: ${this.code}")
        }

        return FilterSettingsDb(id, code, settingsStr)
    }
}