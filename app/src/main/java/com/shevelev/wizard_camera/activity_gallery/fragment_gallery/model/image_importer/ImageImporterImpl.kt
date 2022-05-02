package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model.image_importer

import android.net.Uri
import com.shevelev.wizard_camera.core.bitmaps.api.type_detector.ImageType
import com.shevelev.wizard_camera.core.bitmaps.api.type_detector.ImageTypeDetector
import com.shevelev.wizard_camera.core.bitmaps.api.bitmaps.BitmapHelper
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.EmptyFilterSettings
import com.shevelev.wizard_camera.core.database.api.repositories.PhotoShotRepository
import com.shevelev.wizard_camera.core.photo_files.api.FilesHelper
import com.shevelev.wizard_camera.core.photo_files.api.MediaScanner
import com.shevelev.wizard_camera.core.photo_files.api.new.PhotoFilesRepository
import com.shevelev.wizard_camera.core.utils.id.IdUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class ImageImporterImpl
constructor(
    private val bitmapHelper: BitmapHelper,
    private val imageTypeDetector: ImageTypeDetector,
    private val photoFilesRepository: PhotoFilesRepository
) : ImageImporter {

    override suspend fun import(uri: Uri): PhotoShot? {
        val imageType = withContext(Dispatchers.IO) {
            imageTypeDetector.getImageType(uri)
        }

        if(imageType == ImageType.UNDEFINED) {
            return null
        }

        return withContext(Dispatchers.IO) {
            val stream = photoFilesRepository.startCapturing()

            bitmapHelper.save(stream, uri)

            photoFilesRepository.completeCapturing(stream, EmptyFilterSettings(GlFilterCode.ORIGINAL))
        }
    }
}