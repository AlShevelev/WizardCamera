package com.shevelev.wizard_camera.gallery_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.gallery_page.di.GalleryPageFragmentComponent
import com.shevelev.wizard_camera.shared.files.FilesHelper
import com.shevelev.wizard_camera.shared.glide.GlideTarget
import com.shevelev.wizard_camera.shared.glide.clear
import com.shevelev.wizard_camera.shared.glide.load
import com.shevelev.wizard_camera.shared.mvvm.view.FragmentBase
import com.shevelev.wizard_camera.utils.useful_ext.ifNotNull
import kotlinx.android.synthetic.main.fragment_gallery_page.*
import javax.inject.Inject

class GalleryPageFragment : FragmentBase() {
    companion object {
        private const val ARG_PHOTO = "PHOTO"

        fun newInstance(item: PhotoShot): GalleryPageFragment =
            GalleryPageFragment().apply { arguments = Bundle().apply { putParcelable(ARG_PHOTO, item) } }
    }

    @Inject
    internal lateinit var filesHelper: FilesHelper

    private var glideCancel: GlideTarget? = null

    override fun inject(key: String) = App.injections.get<GalleryPageFragmentComponent>(key).inject(this)

    override fun releaseInjection(key: String) = App.injections.release<GalleryPageFragmentComponent>(key)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gallery_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val photo = arguments!!.getParcelable<PhotoShot>(ARG_PHOTO)!!
        glideCancel = imageContainer.load(filesHelper.getShotFileByName(photo.fileName), R.drawable.ic_sad_face)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        ifNotNull(glideCancel, context) { glideCancel, context ->
            glideCancel.clear(context)
        }
    }
}