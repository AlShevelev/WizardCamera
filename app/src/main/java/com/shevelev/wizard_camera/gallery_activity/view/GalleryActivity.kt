package com.shevelev.wizard_camera.gallery_activity.view

import android.os.Bundle
import androidx.lifecycle.Observer
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.databinding.ActivityGalleryBinding
import com.shevelev.wizard_camera.gallery_activity.di.GalleryActivityComponent
import com.shevelev.wizard_camera.gallery_activity.view.adapter.GalleryAdapter
import com.shevelev.wizard_camera.gallery_activity.view_model.GalleryActivityViewModel
import com.shevelev.wizard_camera.shared.mvvm.view.ActivityBaseMVVM
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : ActivityBaseMVVM<ActivityGalleryBinding, GalleryActivityViewModel>() {
    override fun provideViewModelType(): Class<GalleryActivityViewModel> = GalleryActivityViewModel::class.java

    override fun layoutResId(): Int = R.layout.activity_gallery

    override fun inject(key: String) = App.injections.get<GalleryActivityComponent>(key).inject(this)

    override fun releaseInjection(key: String) = App.injections.release<GalleryActivityComponent>(key)

    override fun linkViewModel(binding: ActivityGalleryBinding, viewModel: GalleryActivityViewModel) {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        galleryPager.adapter = GalleryAdapter(this, viewModel.pageSize, viewModel)
        viewModel.photos.observe(this, Observer { (galleryPager.adapter as GalleryAdapter).updateItems(it) })
    }
}
