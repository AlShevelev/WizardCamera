package com.shevelev.wizard_camera.storage.mapping

import android.net.Uri
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.storage.entities.PhotoShotDb

fun PhotoShot.map(): PhotoShotDb =
    PhotoShotDb(
        id = id,
        fileName = fileName,
        created = created,
        createdSort = created.toEpochSecond(),
        filter = filter,
        contentUri = contentUri?.toString()
    )

fun PhotoShotDb.map(): PhotoShot =
    PhotoShot(
        id = id,
        fileName = fileName,
        created = created,
        filter = filter,
        contentUri = contentUri?.let { Uri.parse(it)}
    )