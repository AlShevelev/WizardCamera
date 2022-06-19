package com.shevelev.wizard_camera.activity_gallery.fragment_editor.view

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.model.dto.ImageWithFilter
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.view_model.EditorFragmentCommand
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.view_model.EditorFragmentViewModel
import com.shevelev.wizard_camera.core.camera_gl.api.bitmap.GLSurfaceViewBitmap
import com.shevelev.wizard_camera.core.camera_gl.api.bitmap.filters.GlSurfaceShaderFilter
import com.shevelev.wizard_camera.core.camera_gl.api.shared.factory.GlShaderFiltersFactory
import com.shevelev.wizard_camera.core.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.core.ui_utils.dialogs.ConfirmationDialog
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view.FragmentBaseMVVM
import com.shevelev.wizard_camera.databinding.FragmentEditorBinding
import com.shevelev.wizard_camera.feature.filters_facade.api.di.FiltersFacadeInjectionSettings
import com.shevelev.wizard_camera.feature.filters_facade.impl.di.FiltersFacadeScope
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val FILTERS_SCOPE_ID = "EDITOR_FRAGMENT_FILTERS_SCOPE_ID"

internal class EditorFragment : FragmentBaseMVVM<FragmentEditorBinding, EditorFragmentViewModel, EditorFragmentCommand>() {
    private var glFilter: GlSurfaceShaderFilter? = null

    private val filtersFactory: GlShaderFiltersFactory by inject()

    private var displayedImage: Bitmap? = null

    override val isBackHandlerEnabled: Boolean
        get() = true

    override val viewModel: EditorFragmentViewModel by viewModel {
        parametersOf(
            FiltersFacadeInjectionSettings(
                FILTERS_SCOPE_ID,
                useInMemoryLastUsedFilters = true,
                canUpdateFavorites = false
            ),
            requireArguments().getParcelable<PhotoShot>(ARG_PHOTO)!!
        )
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

        viewModel.flowerFilters.observe(viewLifecycleOwner) {
            binding.flowerMenu.init(it)
        }
        binding.flowerMenu.setOnItemClickListener { viewModel.onFilterFromMenuClick(it) }

        binding.glFiltersSettings.setOnSettingsChangeListener(viewModel::onGLFilterSettingsUpdated)

        binding.surfaceContainer.setOnClickListener { viewModel.onGlSurfaceClick() }
    }

    override fun onDestroy() {
        super.onDestroy()
        FiltersFacadeScope.close(FILTERS_SCOPE_ID)
    }

    override fun processViewCommand(command: EditorFragmentCommand) {
        when(command) {
            EditorFragmentCommand.ShowEditorSaveDialog ->
                ConfirmationDialog.show(
                    SAVE_DIALOG_REQUEST,
                    this,
                    R.string.savePhotoQuestion,
                    R.string.savePhotoSave,
                    R.string.savePhotoCancel
                )

            EditorFragmentCommand.CloseEditor -> findNavController().popBackStack()

            is EditorFragmentCommand.SetFlowerMenuVisibility -> {
                if(command.isVisible) {
                    binding.flowerMenu.show()
                } else {
                    binding.flowerMenu.hide()
                }
            }
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