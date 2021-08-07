package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model.image_importer

import android.net.Uri
import com.shevelev.wizard_camera.bitmap_images.bitmap_utils.BitmapHelper
import com.shevelev.wizard_camera.bitmap_images.image_type_detector.ImageType
import com.shevelev.wizard_camera.bitmap_images.image_type_detector.ImageTypeDetector
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.EmptyFilterSettings
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.shared.files.FilesHelper
import com.shevelev.wizard_camera.shared.media_scanner.MediaScanner
import com.shevelev.wizard_camera.storage.repositories.PhotoShotRepository
import com.shevelev.wizard_camera.utils.id.IdUtil
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject

class ImageImporterImpl
@Inject
constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val photoShotRepository: PhotoShotRepository,
    private val filesHelper: FilesHelper,
    private val mediaScanner: MediaScanner,
    private val bitmapHelper: BitmapHelper,
    private val imageTypeDetector: ImageTypeDetector
) : ImageImporter {

    override suspend fun import(uri: Uri): PhotoShot? {
        val imageType = withContext(dispatchersProvider.ioDispatcher) {
            imageTypeDetector.getImageType(uri)
        }

        if(imageType == ImageType.UNDEFINED) {
            return null
        }

        val imageFile = withContext(dispatchersProvider.ioDispatcher) {
            filesHelper.createFileForShot().also {
                bitmapHelper.saveBitmap(it, uri)
                bitmapHelper.checkAndCorrectOrientation(it)
            }
        }

        val contentUri = mediaScanner.processNewShot(imageFile)

        return withContext(dispatchersProvider.ioDispatcher) {
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