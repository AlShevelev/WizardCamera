package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import com.shevelev.wizard_camera.BuildConfig
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.dto.GalleryItem
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.dto.ShotsLoadingResult
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model.image_importer.ImageImporter
import com.shevelev.wizard_camera.activity_gallery.shared.FragmentsDataPass
import com.shevelev.wizard_camera.core.bitmaps.api.utils.BitmapHelper
import com.shevelev.wizard_camera.core.photo_files.api.FilesHelper
import com.shevelev.wizard_camera.core.photo_files.api.MediaScanner
import com.shevelev.wizard_camera.core.database.api.repositories.PhotoShotRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class GalleryFragmentInteractorImpl
@Inject
constructor(
    private val appContext: Context,
    private val photoShotRepository: PhotoShotRepository,
    private val filesHelper: FilesHelper,
    private val mediaScanner: MediaScanner,
    private val fragmentsDataPass: FragmentsDataPass,
    private val bitmapHelper: BitmapHelper,
    private val imageImporter: ImageImporter
) : GalleryFragmentInteractor {

    companion object {
        private const val PAGE_SIZE = 20
    }

    private var offset = 0

    private val photosList = mutableListOf<GalleryItem>()

    private var updateInProgress = false

    private val loadingResultChannel = BroadcastChannel<ShotsLoadingResult>(1)
    override val loadingResult: Flow<ShotsLoadingResult> = loadingResultChannel.asFlow()

    override val pageSize: Int = PAGE_SIZE

    override suspend fun initPhotos() =
        processAction {
            if(photosList.isEmpty()) {
                loadingResultChannel.send(ShotsLoadingResult.PreLoading)

                loadNextPageInternal()
            } else {
                fragmentsDataPass.extractPhotoShot()?.let { editedPhotoShot ->
                    val itemIndex = photosList.indexOfFirst { it.item.id == editedPhotoShot.id }
                    if(itemIndex != -1) {
                        photosList[itemIndex] =
                            photosList[itemIndex]
                                .copy(
                                    version = photosList[itemIndex].version + 1,
                                    item = editedPhotoShot
                                )

                        loadingResultChannel.send(ShotsLoadingResult.DataUpdated(photosList))
                    }
                }
            }
        }

    override suspend fun loadNextPage() =
        processAction {
            loadNextPageInternal()
        }

    override suspend fun delete(position: Int) =
        processAction {
            val shotItem = photosList[position]

            val deletedFile = withContext(Dispatchers.IO) {
                photoShotRepository.deleteById(shotItem.item.id)
                filesHelper.removeShotFileByName(shotItem.item.fileName)
            }

            mediaScanner.processDeletedShot(deletedFile)

            photosList.removeAt(position)
            loadingResultChannel.send(ShotsLoadingResult.DataUpdated(photosList))
        }

    override fun getShot(position: Int): PhotoShot = photosList[position].item

    override fun clear() {
        loadingResultChannel.close()
    }

    /**
     * Saves a bitmap into a temporary file and returns content Uri for sharing
     */
    override suspend fun startBitmapSharing(bitmap: Bitmap): Uri {
        val file = withContext(Dispatchers.IO) {
            filesHelper.createTempFileForShot().also {
                bitmapHelper.saveBitmap(it, bitmap)
            }
        }

        return FileProvider.getUriForFile(appContext, "${BuildConfig.APPLICATION_ID}.file_provider", file)
    }

    override suspend fun importBitmap(sourceUri: Uri, currentPosition: Int): Boolean {
        val shot = withContext(Dispatchers.IO) {
            try {
                imageImporter.import(sourceUri)
            } catch (ex: Exception) {
                Timber.e(ex)
                null
            }
        } ?: return false

        photosList.add(currentPosition, GalleryItem(shot.id, 0, shot))
        loadingResultChannel.send(ShotsLoadingResult.DataUpdated(photosList))

        return true
    }

    private suspend fun loadNextPageInternal() {
        val dbData = withContext(Dispatchers.IO) {
            photoShotRepository.readPaged(PAGE_SIZE, offset)
        }
            .map { GalleryItem(id = it.id, version = 0, item = it) }

        photosList.addAll(dbData)
        offset += PAGE_SIZE
        loadingResultChannel.send(ShotsLoadingResult.DataUpdated(photosList))
    }

    private suspend fun processAction(action: suspend () -> Unit) {
        if(updateInProgress) {
            return
        }

        try {
            updateInProgress = true

            action()
        } catch (ex: Exception) {
            Timber.e(ex)
            throw ex
        } finally {
            updateInProgress = false
        }
    }
}