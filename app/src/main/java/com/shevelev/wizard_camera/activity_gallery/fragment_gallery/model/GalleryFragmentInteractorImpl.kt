package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model

import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model.dto.ShotsLoadingResult
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.shared.files.FilesHelper
import com.shevelev.wizard_camera.shared.media_scanner.MediaScanner
import com.shevelev.wizard_camera.storage.repositories.PhotoShotRepository
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
    private val dispatchersProvider: DispatchersProvider,
    private val photoShotRepository: PhotoShotRepository,
    private val filesHelper: FilesHelper,
    private val mediaScanner: MediaScanner
) : GalleryFragmentInteractor {

    companion object {
        private const val PAGE_SIZE = 20
    }

    private var offset = 0

    private val photosList = mutableListOf<PhotoShot>()

    private var updateInProgress = false

    private val loadingResultChannel = BroadcastChannel<ShotsLoadingResult>(1)
    override val loadingResult: Flow<ShotsLoadingResult> = loadingResultChannel.asFlow()

    override val pageSize: Int = PAGE_SIZE

    override suspend fun setUp() {
        loadingResultChannel.send(ShotsLoadingResult.PreLoading)
    }

    override suspend fun loadPage() {
        if(updateInProgress) {
            return
        }

        try {
            updateInProgress = true

            val dbData = withContext(dispatchersProvider.ioDispatcher) {
                photoShotRepository.readPaged(PAGE_SIZE, offset)
            }

            photosList.addAll(dbData)
            offset += PAGE_SIZE
            loadingResultChannel.send(ShotsLoadingResult.DataUpdated(photosList))
        } catch (ex: Exception) {
            Timber.e(ex)
            throw ex
        } finally {
            updateInProgress = false
        }
    }

    override suspend fun delete(position: Int) {
        if(updateInProgress) {
            return
        }

        try {
            updateInProgress = true

            val shotItem = photosList[position]

            val deletedFile = withContext(dispatchersProvider.ioDispatcher) {
                photoShotRepository.deleteById(shotItem.id)
                filesHelper.removeShotFileByName(shotItem.fileName)
            }

            mediaScanner.processDeletedShot(deletedFile)

            photosList.removeAt(position)
            loadingResultChannel.send(ShotsLoadingResult.DataUpdated(photosList))
        } catch (ex: Exception) {
            Timber.e(ex)
            throw ex
        } finally {
            updateInProgress = false
        }
    }

    override fun getShot(position: Int): PhotoShot = photosList[position]

    override fun clear() {
        loadingResultChannel.close()
    }
}