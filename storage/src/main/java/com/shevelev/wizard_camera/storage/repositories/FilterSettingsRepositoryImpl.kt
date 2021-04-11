package com.shevelev.wizard_camera.storage.repositories

import com.shevelev.wizard_camera.common_entities.filter_settings.FilterSettings
import com.shevelev.wizard_camera.storage.core.DbCore
import com.shevelev.wizard_camera.storage.entities.FilterSettingsDb
import com.shevelev.wizard_camera.storage.type_converters.filter_settings.FilerSettingsConverter
import com.shevelev.wizard_camera.utils.id.IdUtil
import javax.inject.Inject

class FilterSettingsRepositoryImpl
@Inject
constructor(
    private val db: DbCore,
    private val filerSettingsConverter: FilerSettingsConverter
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

    private fun FilterSettingsDb.map(): FilterSettings = filerSettingsConverter.fromString(code, settings)

    private fun FilterSettings.map(id: Long): FilterSettingsDb =
        FilterSettingsDb(id, code, filerSettingsConverter.toString(this))
}