package com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.activity_gallery.fragment_editor.view.EditorFragment
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.di.GalleryFragmentComponent
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.dto.EditShotCommand
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.dto.ShareShotCommand
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view.adapter.GalleryAdapter
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view.external_actions.GalleryHelper
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view.external_actions.SharingHelper
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery.view_model.GalleryFragmentViewModel
import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.databinding.FragmentGalleryBinding
import com.shevelev.wizard_camera.shared.dialogs.ConfirmationDialog
import com.shevelev.wizard_camera.shared.mvvm.view.FragmentBaseMVVM
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ScrollGalleryToPosition
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ViewCommand
import dagger.Lazy
import javax.inject.Inject

private const val DELETE_DIALOG_REQUEST = 14109

class GalleryFragment : FragmentBaseMVVM<FragmentGalleryBinding, GalleryFragmentViewModel>() {
    @Inject
    internal lateinit var galleryHelper: Lazy<GalleryHelper>

    @Inject
    internal lateinit var sharingHelper: Lazy<SharingHelper>

    override val viewModel: GalleryFragmentViewModel by viewModels { viewModelFactory }

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
        viewModel.currentPageIndex?.let { pageIndex ->
            binding.galleryPager.post {
                binding.galleryPager.setCurrentItem(pageIndex, false)
            }
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            (binding.galleryPager.adapter as GalleryAdapter).updateItems(it)
        }

        binding.deleteButton.setOnClickListener {
            ConfirmationDialog.show(
                DELETE_DIALOG_REQUEST,
                this,
                R.string.deletePhotoQuestion,
                R.string.delete,
                R.string.cancel
            )
        }

        binding.shareButton.setOnClickListener {
            with(binding.galleryPager) {
                (adapter as GalleryAdapter).getFragment(currentItem)?.let { fragment ->
                    fragment.getBitmap { bitmap ->
                        bitmap?.let {
                            viewModel.onShareShotClick(bitmap)
                        }
                    }
                }
            }
        }

        binding.editButton.setOnClickListener { viewModel.onEditShotClick(binding.galleryPager.currentItem) }

        binding.importButton.setOnClickListener { startImageImport() }

        binding.galleryPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.onShotSelected(position)
            }
        })

        viewModel.initPhotos()
    }

    override fun processViewCommand(command: ViewCommand) {
        when(command) {
            is ShareShotCommand -> sharingHelper.get().startSharing(command.contentUri, this)

            is EditShotCommand ->
                findNavController().navigate(
                    R.id.action_galleryFragment_to_editorFragment,
                    EditorFragment.createParameters(command.shot)
                )

            is ScrollGalleryToPosition -> binding.galleryPager.postDelayed({
                binding.galleryPager.setCurrentItem(command.position, true)
            }, 250L)
        }
    }

    override fun onDialogResult(isCanceled: Boolean, requestCode: Int, data: Any?) {
        when(requestCode) {
            DELETE_DIALOG_REQUEST -> if(!isCanceled) {
                viewModel.onDeleteShotClick(binding.galleryPager.currentItem)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        galleryHelper.get().processTakingPhotoResult(requestCode, resultCode, data) { uri ->
            viewModel.startImageImport(uri, binding.galleryPager.currentItem)
        }
    }

    private fun startImageImport() {
        galleryHelper.get().startTakingPhoto(this)
    }
}