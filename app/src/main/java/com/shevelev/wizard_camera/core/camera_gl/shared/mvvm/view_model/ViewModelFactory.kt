package com.shevelev.wizard_camera.core.camera_gl.shared.mvvm.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
abstract class ViewModelFactory(private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModels[modelClass]?.get() as T
}

interface FragmentViewModelFactory: ViewModelProvider.Factory

class FragmentViewModelFactoryImpl
@Inject
constructor(
    viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelFactory(viewModels), FragmentViewModelFactory

interface ActivityViewModelFactory: ViewModelProvider.Factory

class ActivityViewModelFactoryImpl
@Inject
constructor(
    viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelFactory(viewModels), ActivityViewModelFactory
