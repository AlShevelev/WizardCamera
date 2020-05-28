package com.shevelev.wizard_camera.gallery_activity.view_model

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.gallery_activity.dto.ShareShotCommand
import com.shevelev.wizard_camera.gallery_activity.dto.ShotsLoadingResult
import com.shevelev.wizard_camera.gallery_activity.model.GalleryActivityModel
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ShowMessageResCommand
import com.shevelev.wizard_camera.shared.mvvm.view_model.ViewModelBase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import javax.inject.Inject
import kotlinx.coroutines.flow.collect

class GalleryActivityViewModel
@Inject
constructor(
    dispatchersProvider: DispatchersProvider,
    model: GalleryActivityModel
) : ViewModelBase<GalleryActivityModel>(dispatchersProvider, model),
    GalleryPagingActions {

    private val _photos = MutableLiveData<List<PhotoShot>>()
    val photos: LiveData<List<PhotoShot>> = _photos

    val pageSize: Int = model.pageSize

    private val _isShareButtonVisible = MutableLiveData(View.INVISIBLE)
    val isShareButtonVisible: LiveData<Int> = _isShareButtonVisible

    private val _shotDateTime = MutableLiveData("")
    val shotDateTime: LiveData<String> = _shotDateTime

    private val _isNoDataStubVisible = MutableLiveData(View.INVISIBLE)
    val isNoDataStubVisible: LiveData<Int> = _isNoDataStubVisible

    private val _isShotWidgetsVisible = MutableLiveData(View.INVISIBLE)
    val isShotWidgetsVisible: LiveData<Int> = _isShotWidgetsVisible

    init {
        launch {
            model.loadingResult.collect {
                when(it) {
                    is ShotsLoadingResult.PreLoading -> {
                        _isNoDataStubVisible.value = View.INVISIBLE
                        _isShotWidgetsVisible.value = View.INVISIBLE
                        _isShareButtonVisible.value = View.INVISIBLE
                    }
                    is ShotsLoadingResult.DataUpdated -> {
                        _isNoDataStubVisible.value = if (it.data.isEmpty()) View.VISIBLE else View.INVISIBLE
                        _isShotWidgetsVisible.value = if (it.data.isNotEmpty()) View.VISIBLE else View.INVISIBLE
                        _isShareButtonVisible.value = if (it.data.isNotEmpty()) View.VISIBLE else View.INVISIBLE

                        _photos.value = it.data
                    }
                }
            }
        }

        launch {
            model.setUp()
            loadPage()
        }
    }

    override fun loadPage() {
        launch {
            try {
                model.loadPage()
            } catch(ex: Exception) {
                _command.value = ShowMessageResCommand(R.string.generalError)
            }
        }
    }

    fun deleteShot(position: Int) {
        launch {
            try {
                model.delete(position)
            } catch(ex: Exception) {
                _command.value = ShowMessageResCommand(R.string.generalError)
            }
        }
    }

    fun shareShot(position: Int) {
        _command.value = ShareShotCommand(model.getShot(position))
    }

    fun onShotSelected(position: Int) {
        val shot = model.getShot(position)

        _shotDateTime.value = shot.created.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
        _isShareButtonVisible.value = shot.contentUri?.let { View.VISIBLE } ?: View.INVISIBLE
    }

    override fun onCleared() {
        super.onCleared()
        model.clear()
    }
}