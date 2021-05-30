package com.shevelev.wizard_camera.activity_gallery.fragment_editor.view_model

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.EditorFragmentInteractor
import com.shevelev.wizard_camera.shared.coroutines.DispatchersProvider
import com.shevelev.wizard_camera.shared.mvvm.view_model.ViewModelBase
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditorFragmentViewModel
@Inject
constructor(
    dispatchersProvider: DispatchersProvider,
    interactor: EditorFragmentInteractor
) : ViewModelBase<EditorFragmentInteractor>(dispatchersProvider, interactor) {

    private val _progressVisibility = MutableLiveData(View.GONE)
    val progressVisibility: LiveData<Int> = _progressVisibility

    init {
        launch {
            _progressVisibility.value = View.VISIBLE
            interactor.init()
            _progressVisibility.value = View.GONE
        }
    }
}