package com.shevelev.wizard_camera.storage.repositories

import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.storage.core.DbCore
import com.shevelev.wizard_camera.storage.entities.LastUsedFilterDb
import com.shevelev.wizard_camera.utils.id.IdUtil
import javax.inject.Inject

class LastUsedFilterRepositoryImpl
@Inject
constructor(
    private val db: DbCore
) : LastUsedFilterRepository {
    override fun update(code: FilterCode) =
        db.runConsistent {
            val dbRecord = db.lastUsedFilter.read().firstOrNull()
            if(dbRecord == null) {
                db.lastUsedFilter.create(LastUsedFilterDb(IdUtil.generateLongId(), code))
            } else {
                db.lastUsedFilter.update(dbRecord.copy(code = code))
            }
        }

    override fun read(): FilterCode? = db.lastUsedFilter.read().firstOrNull()?.code
}