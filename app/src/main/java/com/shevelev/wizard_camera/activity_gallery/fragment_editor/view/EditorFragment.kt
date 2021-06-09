package com.shevelev.wizard_camera.activity_gallery.fragment_editor.view

import android.os.Bundle
import android.view.View
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.di.EditorFragmentComponent
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.dto.ImageWithFilter
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.view_model.EditorFragmentViewModel
import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.bitmap.GLSurfaceViewBitmap
import com.shevelev.wizard_camera.bitmap.filters.GLSurfaceShaderFilter
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.databinding.FragmentEditorBinding
import com.shevelev.wizard_camera.shared.factory.FiltersFactory
import com.shevelev.wizard_camera.shared.mvvm.view.FragmentBaseMVVM
import com.shevelev.wizard_camera.utils.resources.getScreenSize

class EditorFragment : FragmentBaseMVVM<FragmentEditorBinding, EditorFragmentViewModel>() {
    override fun provideViewModelType(): Class<EditorFragmentViewModel> = EditorFragmentViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_editor

    override fun linkViewModel(binding: FragmentEditorBinding, viewModel: EditorFragmentViewModel) {
        binding.viewModel = viewModel
    }

    override fun inject() {
        val photoSettings = requireArguments().getParcelable<PhotoShot>(ARG_PHOTO)!!
        App.injections.get<EditorFragmentComponent>(photoSettings).inject(this)

    }

    override fun releaseInjection() {
        App.injections.release<EditorFragmentComponent>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initialImage.observe({viewLifecycleOwner.lifecycle}, { setInitialImage(it) })
    }

    private fun setInitialImage(image: ImageWithFilter?) {
        image?.let {
            val filter =  GLSurfaceShaderFilter(
                requireContext(),
                it.image,
                FiltersFactory.getFilterRes(it.settings.code),
                requireContext().getScreenSize(),
                FiltersFactory.createGLFilterSettings(it.settings, requireContext())
            )

            GLSurfaceViewBitmap.createAndAddToView(requireContext(), binding.surfaceContainer, it.image, filter)
        }
    }


    companion object {
        private const val ARG_PHOTO = "PHOTO"

        fun createParameters(shot: PhotoShot): Bundle =
            Bundle().apply {
                putParcelable(ARG_PHOTO, shot)
            }
    }
}