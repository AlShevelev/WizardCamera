package com.shevelev.wizard_camera.activity_gallery.fragment_editor.view_model

import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.EditorFragmentInteractor
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.shared.mvvm.view_model.ViewModelBase
import javax.inject.Inject

class EditorFragmentViewModel
@Inject
constructor(
    dispatchersProvider: DispatchersProvider,
    interactor: EditorFragmentInteractor
) : ViewModelBase<EditorFragmentInteractor>(dispatchersProvider, interactor) {
}