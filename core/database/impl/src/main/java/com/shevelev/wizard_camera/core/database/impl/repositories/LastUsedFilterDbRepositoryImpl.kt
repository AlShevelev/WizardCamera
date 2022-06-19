package com.shevelev.wizard_camera.core.database.impl.repositories

import com.shevelev.wizard_camera.core.common_entities.entities.LastUsedFilter
import com.shevelev.wizard_camera.core.database.api.repositories.LastUsedFilterDbRepository
import com.shevelev.wizard_camera.core.database.impl.core.DbCore
import com.shevelev.wizard_camera.core.database.impl.entities.LastUsedFilterDb
import com.shevelev.wizard_camera.core.utils.id.IdUtil

internal class LastUsedFilterDbRepositoryImpl(
    private val db: DbCore
) : LastUsedFilterDbRepository {

    override fun update(filter: LastUsedFilter) =
        db.runConsistent {
            val dbRecord = db.lastUsedFilter.read().firstOrNull { it.group == filter.group }

            if(dbRecord == null) {
                db.lastUsedFilter.create(LastUsedFilterDb(IdUtil.generateLongId(), filter.code, filter.group ))
            } else {
                db.lastUsedFilter.update(dbRecord.copy(code = filter.code))
            }
        }

    override fun remove(filter: LastUsedFilter) = db.lastUsedFilter.delete(filter.group)

    override fun read(): List<LastUsedFilter> = db.lastUsedFilter.read().map { LastUsedFilter(it.code, it.group) }
}