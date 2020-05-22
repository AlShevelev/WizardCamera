package com.shevelev.wizard_camera.gallery_activity.view_model

import com.shevelev.wizard_camera.gallery_activity.model.GalleryActivityModel
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.shared.mvvm.view_model.ViewModelBase
import javax.inject.Inject

class GalleryActivityViewModel
@Inject
constructor(
    dispatchersProvider: DispatchersProvider,
    model: GalleryActivityModel
) : ViewModelBase<GalleryActivityModel>(dispatchersProvider, model) {

}