package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model.image_importer

import android.net.Uri
import com.shevelev.wizard_camera.core.bitmaps.api.type_detector.ImageType
import com.shevelev.wizard_camera.core.bitmaps.api.type_detector.ImageTypeDetector
import com.shevelev.wizard_camera.core.bitmaps.api.bitmaps.BitmapHelper
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.core.common_entities.enums.GlFilterCode
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.EmptyFilterSettings
import com.shevelev.wizard_camera.core.photo_files.api.photo_shot_repository.PhotoShotRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class ImageImporterImpl (
    private val bitmapHelper: BitmapHelper,
    private val imageTypeDetector: ImageTypeDetector,
    private val photoShotRepository: PhotoShotRepository
) : ImageImporter {

    override suspend fun import(uri: Uri): PhotoShot? {
        val imageType = withContext(Dispatchers.IO) {
            imageTypeDetector.getImageType(uri)
        }

        if(imageType == ImageType.UNDEFINED) {
            return null
        }

        return withContext(Dispatchers.IO) {
            photoShotRepository.startCapturing()?.let { result ->
                bitmapHelper.copy(uri, result.capturingStream)

                photoShotRepository.completeCapturing(result.key, EmptyFilterSettings(GlFilterCode.ORIGINAL))
            }
        }
    }
}