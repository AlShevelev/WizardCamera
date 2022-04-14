package com.shevelev.wizard_camera.database.impl.repositories

import com.shevelev.wizard_camera.common_entities.entities.LastUsedFilter
import com.shevelev.wizard_camera.database.api.repositories.LastUsedFilterRepository
import com.shevelev.wizard_camera.database.impl.core.DbCore
import com.shevelev.wizard_camera.database.impl.entities.LastUsedFilterDb
import com.shevelev.wizard_camera.utils.id.IdUtil
import javax.inject.Inject

class LastUsedFilterRepositoryImpl
@Inject
constructor(
    private val db: DbCore
) : LastUsedFilterRepository {

    override fun update(filter: LastUsedFilter) =
        db.runConsistent {
            val dbRecord = db.lastUsedFilter.read().firstOrNull { it.isFavorite == filter.isFavorite }

            if(dbRecord == null) {
                db.lastUsedFilter.create(LastUsedFilterDb(IdUtil.generateLongId(), filter.code, filter.isFavorite ))
            } else {
                db.lastUsedFilter.update(dbRecord.copy(code = filter.code))
            }
        }

    override fun remove(filter: LastUsedFilter) = db.lastUsedFilter.delete(filter.code, filter.isFavorite)

    override fun read(): List<LastUsedFilter> = db.lastUsedFilter.read().map { LastUsedFilter(it.code, it.isFavorite) }
}