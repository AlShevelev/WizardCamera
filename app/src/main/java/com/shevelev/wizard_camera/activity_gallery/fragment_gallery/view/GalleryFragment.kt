package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.view.EditorFragment
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.di.GalleryFragmentComponent
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model.dto.EditShotCommand
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.model.dto.ShareShotCommand
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view.adapter.GalleryAdapter
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view_model.GalleryFragmentViewModel
import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.databinding.FragmentGalleryBinding
import com.shevelev.wizard_camera.shared.dialogs.ConfirmationDialog
import com.shevelev.wizard_camera.shared.mvvm.view.FragmentBaseMVVM
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ViewCommand

class GalleryFragment : FragmentBaseMVVM<FragmentGalleryBinding, GalleryFragmentViewModel>() {
    override fun provideViewModelType(): Class<GalleryFragmentViewModel> = GalleryFragmentViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_gallery

    override fun linkViewModel(binding: FragmentGalleryBinding, viewModel: GalleryFragmentViewModel) {
        binding.viewModel = viewModel
    }

    override fun inject() {
        App.injections.get<GalleryFragmentComponent>().inject(this)

    }

    override fun releaseInjection() {
        App.injections.release<GalleryFragmentComponent>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.galleryPager.adapter = GalleryAdapter(this, viewModel.pageSize, viewModel)
        viewModel.photos.observe(
            {viewLifecycleOwner.lifecycle},
            { (binding.galleryPager.adapter as GalleryAdapter).updateItems(it) })

        binding.deleteButton.setOnClickListener {
            ConfirmationDialog.show(childFragmentManager, R.string.deletePhotoQuestion, R.string.delete, R.string.cancel) {
                if(it) {
                    viewModel.onDeleteShotClick(binding.galleryPager.currentItem)
                }
            }
        }

        binding.shareButton.setOnClickListener { viewModel.onShareShotClick(binding.galleryPager.currentItem) }

        binding.editButton.setOnClickListener { viewModel.onEditShotClick(binding.galleryPager.currentItem) }

        binding.galleryPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.onShotSelected(position)
            }
        })
    }

    override fun processViewCommand(command: ViewCommand) {
        when(command) {
            is ShareShotCommand -> shareShot(command.shot)

            is EditShotCommand ->
                findNavController().navigate(
                    R.id.action_galleryFragment_to_editorFragment,
                    EditorFragment.createParameters(command.shot)
                )
        }
    }

    private fun shareShot(shot: PhotoShot) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, shot.contentUri)
            type = "image/jpeg"
        }
        startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.sendTo)))
    }
}