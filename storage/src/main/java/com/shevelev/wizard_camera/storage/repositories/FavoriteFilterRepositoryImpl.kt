package com.shevelev.wizard_camera.storage.repositories

import com.shevelev.wizard_camera.common_entities.enums.FilterCode
import com.shevelev.wizard_camera.storage.core.DbCore
import com.shevelev.wizard_camera.storage.entities.FavoriteFilterDb
import com.shevelev.wizard_camera.utils.id.IdUtil
import javax.inject.Inject

class FavoriteFilterRepositoryImpl
@Inject
constructor(
    private val db: DbCore
) : FavoriteFilterRepository {

    override fun create(code: FilterCode) {
        db.runConsistent {
            if(db.favoriteFilter.exists(code) == null) {
                db.favoriteFilter.create(FavoriteFilterDb(IdUtil.generateLongId(), code))
            }
        }
    }

    override fun read(): List<FilterCode> = db.favoriteFilter.read().map { it.code }

    override fun delete(code: FilterCode) = db.favoriteFilter.delete(code)
}