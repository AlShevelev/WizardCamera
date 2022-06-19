package com.shevelev.wizard_camera.core.database.impl.repositories

import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.database.api.repositories.FavoriteFilterDbRepository
import com.shevelev.wizard_camera.core.database.impl.core.DbCore
import com.shevelev.wizard_camera.core.database.impl.entities.FavoriteFilterDb
import com.shevelev.wizard_camera.core.utils.id.IdUtil

internal class FavoriteFilterDbRepositoryImpl(
    private val db: DbCore
) : FavoriteFilterDbRepository {

    override fun create(code: GlFilterCode) {
        db.runConsistent {
            if(db.favoriteFilter.exists(code) == null) {
                db.favoriteFilter.create(FavoriteFilterDb(IdUtil.generateLongId(), code))
            }
        }
    }

    override fun read(): List<GlFilterCode> = db.favoriteFilter.read().map { it.code }

    override fun delete(code: GlFilterCode) = db.favoriteFilter.delete(code)
}