package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model

import android.graphics.Bitmap
import android.net.Uri
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.dto.GalleryItem
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.dto.ShotsLoadingResult
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model.image_importer.ImageImporter
import com.shevelev.wizard_camera.activity_gallery.shared.FragmentsDataPass
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.core.database.api.repositories.PhotoShotDbRepository
import com.shevelev.wizard_camera.core.photo_files.api.new.PhotoShotRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext
import timber.log.Timber

@ExperimentalCoroutinesApi
@FlowPreview
class GalleryFragmentInteractorImpl
constructor(
    private val photoShotDbRepository: PhotoShotDbRepository,
    private val fragmentsDataPass: FragmentsDataPass,
    private val imageImporter: ImageImporter,
    private val photoShotRepository: PhotoShotRepository
) : GalleryFragmentInteractor {

    companion object {
        private const val PAGE_SIZE = 20
    }

    private var offset = 0

    private val photosList = mutableListOf<GalleryItem>()

    private var updateInProgress = false

    private val loadingResultMutable = MutableSharedFlow<ShotsLoadingResult>(
        replay = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1
    )

    override val loadingResult: SharedFlow<ShotsLoadingResult> = loadingResultMutable

    override val pageSize: Int = PAGE_SIZE

    override suspend fun initPhotos() =
        processAction {
            if(photosList.isEmpty()) {
                loadingResultMutable.tryEmit(ShotsLoadingResult.PreLoading)

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

                        loadingResultMutable.tryEmit(ShotsLoadingResult.DataUpdated(photosList))
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

            photoShotRepository.removeShot(shotItem.item)

            photosList.removeAt(position)
            loadingResultMutable.tryEmit(ShotsLoadingResult.DataUpdated(photosList))
        }

    override fun getShot(position: Int): PhotoShot = photosList[position].item

    /**
     * Saves a bitmap into a temporary file and returns content Uri for sharing
     */
    override suspend fun startBitmapSharing(bitmap: Bitmap): Uri = photoShotRepository.saveBitmapToTempStorage(bitmap)

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
        loadingResultMutable.tryEmit(ShotsLoadingResult.DataUpdated(photosList))

        return true
    }

    private suspend fun loadNextPageInternal() {
        val dbData = withContext(Dispatchers.IO) {
            photoShotDbRepository.readPaged(PAGE_SIZE, offset)
        }
            .map { GalleryItem(id = it.id, version = 0, item = it) }

        photosList.addAll(dbData)
        offset += PAGE_SIZE
        loadingResultMutable.tryEmit(ShotsLoadingResult.DataUpdated(photosList))
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