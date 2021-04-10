package com.shevelev.wizard_camera.activity_gallery.fragment_gallery_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.databinding.FragmentGalleryPageBinding
import com.shevelev.wizard_camera.activity_gallery.fragment_gallery_page.di.GalleryPageFragmentComponent
import com.shevelev.wizard_camera.shared.files.FilesHelper
import com.shevelev.wizard_camera.shared.glide.GlideTarget
import com.shevelev.wizard_camera.shared.glide.clear
import com.shevelev.wizard_camera.shared.glide.load
import com.shevelev.wizard_camera.shared.mvvm.view.FragmentBase
import com.shevelev.wizard_camera.utils.useful_ext.ifNotNull
import javax.inject.Inject

class GalleryPageFragment : FragmentBase<FragmentGalleryPageBinding>() {
    companion object {
        private const val ARG_PHOTO = "PHOTO"

        fun newInstance(item: PhotoShot): GalleryPageFragment =
            GalleryPageFragment().apply { arguments = Bundle().apply { putParcelable(ARG_PHOTO, item) } }
    }

    @Inject
    internal lateinit var filesHelper: FilesHelper

    private var glideCancel: GlideTarget? = null

    override fun inject() = App.injections.get<GalleryPageFragmentComponent>().inject(this)

    override fun releaseInjection() = App.injections.release<GalleryPageFragmentComponent>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val photo = arguments!!.getParcelable<PhotoShot>(ARG_PHOTO)!!
        glideCancel = binding.imageContainer.load(filesHelper.getShotFileByName(photo.fileName), R.drawable.ic_sad_face)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        ifNotNull(glideCancel, context) { glideCancel, context ->
            glideCancel.clear(context)
        }
    }

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentGalleryPageBinding =
        FragmentGalleryPageBinding.inflate(inflater, container, false)
}