package com.shevelev.wizard_camera.activity_gallery.fragment_editor.view

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.di.EditorFragmentComponent
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.dto.ImageWithFilter
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.view_model.EditorFragmentViewModel
import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.bitmap.GLSurfaceViewBitmap
import com.shevelev.wizard_camera.bitmap.filters.GLSurfaceShaderFilter
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.databinding.FragmentEditorBinding
import com.shevelev.wizard_camera.shared.dialogs.ConfirmationDialog
import com.shevelev.wizard_camera.shared.factory.FiltersFactory
import com.shevelev.wizard_camera.shared.mvvm.view.FragmentBaseMVVM
import com.shevelev.wizard_camera.shared.mvvm.view_commands.CloseEditorCommand
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ShowEditorSaveDialogCommand
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ViewCommand
import com.shevelev.wizard_camera.utils.resources.getScreenSize

class EditorFragment : FragmentBaseMVVM<FragmentEditorBinding, EditorFragmentViewModel>() {
    private var glFilter: GLSurfaceShaderFilter? = null

    private var displayedImage: Bitmap? = null

    override val isBackHandlerEnabled: Boolean
        get() = true

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

        viewModel.imageWithGlFilter.observe(viewLifecycleOwner) {
            setImageWithGlFilter(it)
        }

        viewModel.glFilters.observe(viewLifecycleOwner) {
            binding.glFiltersCarousel.setStartData(it, viewModel)
        }
        binding.glFiltersCarousel.setOnItemSelectedListener(viewModel::onGLFilterSelected)

        viewModel.glSettings.observe(viewLifecycleOwner) {
            if(it == null) {
                binding.glFiltersSettings.hide()
            } else {
                binding.glFiltersSettings.show(it)
            }
        }
        binding.glFiltersSettings.setOnSettingsChangeListener(viewModel::onGLFilterSettingsUpdated)

        binding.surfaceContainer.setOnClickListener { viewModel.onGlSurfaceClick() }
    }

    override fun processViewCommand(command: ViewCommand) {
        when(command) {
            is ShowEditorSaveDialogCommand ->
                ConfirmationDialog.show(
                    SAVE_DIALOG_REQUEST,
                    this,
                    R.string.savePhotoQuestion,
                    R.string.savePhotoSave,
                    R.string.savePhotoCancel
                )

            is CloseEditorCommand -> findNavController().popBackStack()
        }
    }

    override fun onDialogResult(isCanceled: Boolean, requestCode: Int, data: Any?) {
        when(requestCode) {
            SAVE_DIALOG_REQUEST -> if(isCanceled) {
                viewModel.onCancelClick()
            } else {
                viewModel.onAcceptClick()
            }
        }
    }

    override fun onBackPressed() = viewModel.onCancelClick()

    private fun setImageWithGlFilter(image: ImageWithFilter?) {
        image?.let {
            if(glFilter?.code != it.settings.code || displayedImage != image.image) {
                val filter =  GLSurfaceShaderFilter(
                    requireContext(),
                    it.image,
                    FiltersFactory.getFilterRes(it.settings.code),
                    requireContext().getScreenSize(),
                    FiltersFactory.createGLFilterSettings(it.settings, requireContext())
                )

                glFilter = filter

                GLSurfaceViewBitmap.createAndAddToView(requireContext(), binding.surfaceContainer, it.image, filter)

                displayedImage = image.image
            } else {
                val filter = FiltersFactory.createGLFilterSettings(it.settings, requireContext())
                glFilter!!.updateSettings(filter)
            }
        }
    }


    companion object {
        private const val ARG_PHOTO = "PHOTO"

        private const val SAVE_DIALOG_REQUEST = 45904

        fun createParameters(shot: PhotoShot): Bundle =
            Bundle().apply {
                putParcelable(ARG_PHOTO, shot)
            }
    }
}