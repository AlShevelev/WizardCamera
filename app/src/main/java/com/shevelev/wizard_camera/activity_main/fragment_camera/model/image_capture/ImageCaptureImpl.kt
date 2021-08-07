package com.shevelev.wizard_camera.activity_main.fragment_camera.model.image_capture

import android.net.Uri
import com.shevelev.wizard_camera.activity_main.fragment_camera.model.dto.ScreenOrientation
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.shared.files.FilesHelper
import com.shevelev.wizard_camera.shared.media_scanner.MediaScanner
import com.shevelev.wizard_camera.storage.repositories.PhotoShotRepository
import com.shevelev.wizard_camera.bitmap_images.bitmap_utils.BitmapHelper
import com.shevelev.wizard_camera.utils.id.IdUtil
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class ImageCaptureImpl
@Inject
constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val photoShotRepository: PhotoShotRepository,
    private val filesHelper: FilesHelper,
    private val mediaScanner: MediaScanner,
    private val bitmapHelper: BitmapHelper
) : ImageCapture {

    override val inProgress: Boolean
        get() = targetFile != null

    private var targetFile: File? = null

    private var activeFilter: GlFilterSettings? = null

    /**
     * Starts capturing process
     * @return target file for an image
     */
    override suspend fun startCapture(activeFilter: GlFilterSettings, screenOrientation: ScreenOrientation): File? {
        targetFile = try {
            this.activeFilter = activeFilter

            withContext(dispatchersProvider.ioDispatcher) {
                filesHelper.createFileForShot()
            }
        } catch (ex: Exception) {
            Timber.e(ex)
            null
        }

        return targetFile
    }

    override suspend fun captureCompleted() {
        withContext(dispatchersProvider.ioDispatcher) {
            targetFile?.let { targetFile ->
                bitmapHelper.checkAndCorrectOrientation(targetFile)

                val contentUri = mediaScanner.processNewShot(targetFile)

                saveToDb(targetFile.name, activeFilter!!, contentUri)
            }
        }

        targetFile = null
    }

    private fun saveToDb(fileName: String, filter: GlFilterSettings, contentUri: Uri?) =
        photoShotRepository.create(PhotoShot(IdUtil.generateLongId(), fileName, ZonedDateTime.now(), filter, contentUri))
}