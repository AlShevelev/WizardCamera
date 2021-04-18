package com.shevelev.wizard_camera.activity_gallery.fragment_gallery_page

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery_page.di.GalleryPageFragmentComponent
import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.bitmap.GLSurfaceViewBitmap
import com.shevelev.wizard_camera.bitmap.renderers.fragment.GrayscaleSurfaceRenderer
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.databinding.FragmentGalleryPageBinding
import com.shevelev.wizard_camera.shared.files.FilesHelper
import com.shevelev.wizard_camera.shared.mvvm.view.FragmentBase
import javax.inject.Inject

class GalleryPageFragment : FragmentBase<FragmentGalleryPageBinding>() {
    companion object {
        private const val ARG_PHOTO = "PHOTO"

        fun newInstance(item: PhotoShot): GalleryPageFragment =
            GalleryPageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PHOTO, item)
                }
            }
    }

    @Inject
    internal lateinit var filesHelper: FilesHelper

    override fun inject() = App.injections.get<GalleryPageFragmentComponent>().inject(this)

    override fun releaseInjection() = App.injections.release<GalleryPageFragmentComponent>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val photo = requireArguments().getParcelable<PhotoShot>(ARG_PHOTO)!!

        val photoBitmap = BitmapFactory.decodeFile(filesHelper.getShotFileByName(photo.fileName).absolutePath)

        val renderer = GrayscaleSurfaceRenderer(requireContext(), photoBitmap)

        GLSurfaceViewBitmap.createAndAddToView(requireContext(), binding.imageContainer, photoBitmap, renderer)
    }

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGalleryPageBinding =
        FragmentGalleryPageBinding.inflate(inflater, container, false)
}