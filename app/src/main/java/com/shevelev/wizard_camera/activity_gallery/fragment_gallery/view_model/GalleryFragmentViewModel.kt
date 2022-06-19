package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view_model

import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.dto.GalleryItem
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.dto.ShotsLoadingResult
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model.GalleryFragmentInteractor
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view_model.ViewModelBase
import kotlinx.coroutines.launch
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

internal class GalleryFragmentViewModel(
    interactor: GalleryFragmentInteractor
) : ViewModelBase<GalleryFragmentInteractor, GalleryFragmentCommand>(interactor),
    GalleryPagingActions {

    private val _photos = MutableLiveData<List<GalleryItem>>()
    val photos: LiveData<List<GalleryItem>> = _photos

    val pageSize: Int = interactor.pageSize

    private val _shotDateTime = MutableLiveData("")
    val shotDateTime: LiveData<String> = _shotDateTime

    private val _isNoDataStubVisible = MutableLiveData(View.INVISIBLE)
    val isNoDataStubVisible: LiveData<Int> = _isNoDataStubVisible

    private val _isShotWidgetsVisible = MutableLiveData(View.INVISIBLE)
    val isShotWidgetsVisible: LiveData<Int> = _isShotWidgetsVisible

    private val _isImportVisible = MutableLiveData(View.INVISIBLE)
    val isImportVisible: LiveData<Int> = _isImportVisible

    var currentPageIndex: Int? = null
        private set

    init {
        viewModelScope.launch {
            interactor.loadingResult.collect {
                when (it) {
                    is ShotsLoadingResult.PreLoading -> {
                        _isNoDataStubVisible.value = View.INVISIBLE
                        _isShotWidgetsVisible.value = View.INVISIBLE
                        _isImportVisible.value = View.INVISIBLE
                    }
                    is ShotsLoadingResult.DataUpdated -> {
                        _isNoDataStubVisible.value = if (it.data.isEmpty()) View.VISIBLE else View.INVISIBLE
                        _isShotWidgetsVisible.value = if (it.data.isNotEmpty()) View.VISIBLE else View.INVISIBLE
                        _isImportVisible.value = View.VISIBLE

                        _photos.value = it.data
                    }
                }
            }
        }
    }

    override fun loadNextPage() = processAction { interactor.loadNextPage() }

    fun initPhotos() = processAction { interactor.initPhotos() }

    fun onDeleteShotClick(position: Int) = processAction { interactor.delete(position) }

    fun onShareShotClick(filteredImage: Bitmap) {
        viewModelScope.launch {
            interactor.startBitmapSharing(filteredImage).also {
                sendCommand(GalleryFragmentCommand.ShareShot(it))
            }
        }
    }

    fun onShotSelected(position: Int) {
        val shot = interactor.getShot(position)

        _shotDateTime.value = shot.created.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))

        currentPageIndex = position
    }

    fun onEditShotClick(position: Int) {
        sendCommand(GalleryFragmentCommand.EditShot(interactor.getShot(position)))
    }

    fun startImageImport(uri: Uri, currentPosition: Int) {
        viewModelScope.launch {
            if (!interactor.importBitmap(uri, currentPosition)) {
                sendCommand(GalleryFragmentCommand.ShowMessageRes(R.string.generalError))
            } else {
                sendCommand(GalleryFragmentCommand.ScrollGalleryToPosition(currentPosition))
            }
        }
    }

    private fun processAction(action: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                action()
            } catch (ex: Exception) {
                sendCommand(GalleryFragmentCommand.ShowMessageRes(R.string.generalError))
            }
        }
    }
}