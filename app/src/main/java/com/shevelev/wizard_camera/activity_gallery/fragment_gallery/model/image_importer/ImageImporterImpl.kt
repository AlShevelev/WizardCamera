package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model.image_importer

import android.net.Uri
import com.shevelev.wizard_camera.core.bitmaps.api.type_detector.ImageType
import com.shevelev.wizard_camera.core.bitmaps.api.type_detector.ImageTypeDetector
import com.shevelev.wizard_camera.core.bitmaps.api.utils.BitmapHelper
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.EmptyFilterSettings
import com.shevelev.wizard_camera.core.database.api.repositories.PhotoShotRepository
import com.shevelev.wizard_camera.core.camera_gl.shared.files.FilesHelper
import com.shevelev.wizard_camera.core.camera_gl.shared.media_scanner.MediaScanner
import com.shevelev.wizard_camera.core.utils.id.IdUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject

class ImageImporterImpl
@Inject
constructor(
    private val photoShotRepository: PhotoShotRepository,
    private val filesHelper: FilesHelper,
    private val mediaScanner: MediaScanner,
    private val bitmapHelper: BitmapHelper,
    private val imageTypeDetector: ImageTypeDetector
) : ImageImporter {

    override suspend fun import(uri: Uri): PhotoShot? {
        val imageType = withContext(Dispatchers.IO) {
            imageTypeDetector.getImageType(uri)
        }

        if(imageType == ImageType.UNDEFINED) {
            return null
        }

        val imageFile = withContext(Dispatchers.IO) {
            filesHelper.createFileForShot().also {
                bitmapHelper.saveBitmap(it, uri)
                bitmapHelper.checkAndCorrectOrientation(it)
            }
        }

        val contentUri = mediaScanner.processNewShot(imageFile)

        return withContext(Dispatchers.IO) {
            val shot = PhotoShot(
                IdUtil.generateLongId(),
                imageFile.name,
                ZonedDateTime.now(),
                EmptyFilterSettings(GlFilterCode.ORIGINAL),
                contentUri
            )

            photoShotRepository.create(shot)

            shot
        }
    }
}