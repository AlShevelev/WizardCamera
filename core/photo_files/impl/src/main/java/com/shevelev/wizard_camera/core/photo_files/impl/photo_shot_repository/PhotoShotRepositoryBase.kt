package com.shevelev.wizard_camera.core.photo_files.impl.photo_shot_repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import com.shevelev.wizard_camera.core.bitmaps.api.bitmaps.BitmapHelper
import com.shevelev.wizard_camera.core.build_info.api.BuildInfo
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.database.api.repositories.PhotoShotDbRepository
import com.shevelev.wizard_camera.core.photo_files.api.photo_shot_repository.PhotoShotRepository
import com.shevelev.wizard_camera.core.utils.id.IdUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.absoluteValue

internal abstract class PhotoShotRepositoryBase(
    protected val appContext: Context,
    protected val appInfo: BuildInfo,
    protected val bitmapHelper: BitmapHelper,
    protected val photoShotRepository: PhotoShotDbRepository
) : PhotoShotRepository {
    /**
     * Saves a bitmap into a temporary storage and returns content Uri for the saved bitmap
     */
    override suspend fun saveBitmapToTempStorage(bitmap: Bitmap): Uri {
        val file = withContext(Dispatchers.IO) {
            File(appContext.cacheDir, "${IdUtil.generateLongId().absoluteValue}.jpg").also {
                bitmapHelper.save(bitmap, it)
            }
        }

        return FileProvider.getUriForFile(appContext, "${appInfo.appId}.file_provider", file)
    }

    /**
     * Updates a given shot by a new bitmap or/and a new filter
     * @return updated shot
     */
    override suspend fun updateShot(bitmap: Bitmap, filter: GlFilterSettings, updatedShot: PhotoShot): PhotoShot =
        withContext(Dispatchers.IO) {
            bitmapHelper.save(bitmap, updatedShot.contentUri)

            val shotToSave = updatedShot.copy(filter = filter)
            photoShotRepository.update(shotToSave)

            shotToSave
        }
}