package com.shevelev.wizard_camera.core.database.impl.repositories

import android.net.Uri
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.core.database.api.repositories.PhotoShotDbRepository
import com.shevelev.wizard_camera.core.database.impl.core.DbCore
import com.shevelev.wizard_camera.core.database.impl.entities.PhotoShotDb
import com.shevelev.wizard_camera.core.database.impl.type_converters.filter_settings.FilerSettingsConverter

internal class PhotoShotDbRepositoryImpl
constructor(
    private val db: DbCore,
    private val filerSettingsConverter: FilerSettingsConverter
): PhotoShotDbRepository {
    override fun create(shot: PhotoShot) = db.photoShot.create(shot.map())

    override fun update(shot: PhotoShot) = db.photoShot.update(shot.map())

    override fun readPaged(limit: Int, offset: Int): List<PhotoShot> = db.photoShot.readPaged(limit, offset).map { it.map() }

    override fun deleteById(id: Long) = db.photoShot.deleteById(id)

    private fun PhotoShot.map(): PhotoShotDb =
        PhotoShotDb(
            id = id,
            contentUri = contentUri.toString(),
            fileName = fileName,
            created = created,
            createdSort = created.toEpochSecond(),
            filterCode = filter.code,
            filterSettings = filerSettingsConverter.toString(filter),
            mediaContentUri = mediaContentUri?.toString()
        )

    private fun PhotoShotDb.map(): PhotoShot =
        PhotoShot(
            id = id,
            contentUri = contentUri.let { Uri.parse(it) },
            fileName = fileName,
            created = created,
            filter = filerSettingsConverter.fromString(filterCode, filterSettings),
            mediaContentUri = mediaContentUri?.let { Uri.parse(it)}
        )
}