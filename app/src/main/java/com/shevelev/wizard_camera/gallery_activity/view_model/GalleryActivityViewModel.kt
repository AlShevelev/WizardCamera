package com.shevelev.wizard_camera.gallery_activity.view_model

import androidx.lifecycle.LiveData
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.gallery_activity.model.GalleryActivityModel
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ShowMessageResCommand
import com.shevelev.wizard_camera.shared.mvvm.view_model.ViewModelBase
import kotlinx.coroutines.launch
import javax.inject.Inject

class GalleryActivityViewModel
@Inject
constructor(
    dispatchersProvider: DispatchersProvider,
    model: GalleryActivityModel
) : ViewModelBase<GalleryActivityModel>(dispatchersProvider, model),
    GalleryPagingActions {

    val photos: LiveData<List<PhotoShot>> = model.photos

    val pageSize: Int = model.pageSize

    init {
        loadPage()
    }

    override fun loadPage() {
        launch {
            val isSuccess = model.loadPage()
            if(!isSuccess) {
                _command.value = ShowMessageResCommand(R.string.generalError)
            }
        }
    }
}