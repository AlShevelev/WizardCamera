package com.shevelev.wizard_camera.core.database.impl.repositories

import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.database.api.repositories.FilterSettingsDbRepository
import com.shevelev.wizard_camera.core.database.impl.core.DbCore
import com.shevelev.wizard_camera.core.database.impl.entities.FilterSettingsDb
import com.shevelev.wizard_camera.core.database.impl.type_converters.filter_settings.FilerSettingsConverter
import com.shevelev.wizard_camera.core.utils.id.IdUtil

internal class FilterSettingsDbRepositoryImpl(
    private val db: DbCore,
    private val filerSettingsConverter: FilerSettingsConverter
) : FilterSettingsDbRepository {
    override fun read(): List<GlFilterSettings> = db.filterSettings.read().map { it.map() }

    override fun update(settings: GlFilterSettings) {
        db.runConsistent {
            val dbRecord = db.filterSettings.read(settings.code)

            if(dbRecord == null) {
                db.filterSettings.create(settings.map(IdUtil.generateLongId()))
            } else {
                db.filterSettings.update(settings.map(dbRecord.id))
            }
        }
    }

    private fun FilterSettingsDb.map(): GlFilterSettings = filerSettingsConverter.fromString(code, settings)

    private fun GlFilterSettings.map(id: Long): FilterSettingsDb =
        FilterSettingsDb(id, code, filerSettingsConverter.toString(this))
}