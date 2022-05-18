package com.shevelev.wizard_camera.activity_gallery.fragment_editor.view

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.dto.ImageWithFilter
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.view_model.EditorFragmentViewModel
import com.shevelev.wizard_camera.core.camera_gl.api.bitmap.GLSurfaceViewBitmap
import com.shevelev.wizard_camera.core.camera_gl.api.bitmap.filters.GlSurfaceShaderFilter
import com.shevelev.wizard_camera.core.camera_gl.api.shared.factory.GlShaderFiltersFactory
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.core.ui_utils.dialogs.ConfirmationDialog
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view.FragmentBaseMVVM
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view_commands.CloseEditorCommand
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view_commands.ShowEditorSaveDialogCommand
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view_commands.ViewCommand
import com.shevelev.wizard_camera.databinding.FragmentEditorBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EditorFragment : FragmentBaseMVVM<FragmentEditorBinding, EditorFragmentViewModel>() {
    private var glFilter: GlSurfaceShaderFilter? = null

    private val filtersFactory: GlShaderFiltersFactory by inject()

    private var displayedImage: Bitmap? = null

    override val isBackHandlerEnabled: Boolean
        get() = true

    override val viewModel: EditorFragmentViewModel by viewModel {
        parametersOf(requireArguments().getParcelable<PhotoShot>(ARG_PHOTO)!!)
    }

    override fun layoutResId(): Int = R.layout.fragment_editor

    override fun linkViewModel(binding: FragmentEditorBinding, viewModel: EditorFragmentViewModel) {
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.imageWithGlFilter.observe(viewLifecycleOwner) {
            setImageWithGlFilter(it)
        }

        viewModel.glFilters.observe(viewLifecycleOwner) {
            binding.glFiltersCarousel.updateData(it, viewModel)
        }

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
                val filter =  filtersFactory.createFilter(
                    it.image,
                    it.settings
                )

                glFilter = filter

                GLSurfaceViewBitmap.createAndAddToView(requireContext(), binding.surfaceContainer, it.image, filter)

                displayedImage = image.image
            } else {
                val filter = filtersFactory.createGLFilterSettings(it.settings, requireContext())
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