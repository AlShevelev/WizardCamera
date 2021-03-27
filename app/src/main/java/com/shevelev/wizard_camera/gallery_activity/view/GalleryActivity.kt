package com.shevelev.wizard_camera.gallery_activity.view

import android.content.Intent
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.common_entities.entities.PhotoShot
import com.shevelev.wizard_camera.databinding.ActivityGalleryBinding
import com.shevelev.wizard_camera.gallery_activity.di.GalleryActivityComponent
import com.shevelev.wizard_camera.gallery_activity.dto.ShareShotCommand
import com.shevelev.wizard_camera.gallery_activity.view.adapter.GalleryAdapter
import com.shevelev.wizard_camera.gallery_activity.view_model.GalleryActivityViewModel
import com.shevelev.wizard_camera.shared.dialogs.ConfirmationDialog
import com.shevelev.wizard_camera.shared.mvvm.view.ActivityBaseMVVM
import com.shevelev.wizard_camera.shared.mvvm.view_commands.ViewCommand
import com.shevelev.wizard_camera.shared.ui_utils.hideSystemUI

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

        binding.galleryPager.adapter = GalleryAdapter(this, viewModel.pageSize, viewModel)
        viewModel.photos.observe(this, { (binding.galleryPager.adapter as GalleryAdapter).updateItems(it) })

        binding.deleteButton.setOnClickListener {
            ConfirmationDialog.show(supportFragmentManager, R.string.deletePhotoQuestion, R.string.delete, R.string.cancel) {
                if(it) {
                    viewModel.deleteShot(binding.galleryPager.currentItem)
                }
            }
        }

        binding.shareButton.setOnClickListener { viewModel.shareShot(binding.galleryPager.currentItem) }

        binding.galleryPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.onShotSelected(position)
            }
        })
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    override fun processViewCommand(command: ViewCommand) {
        when(command) {
            is ShareShotCommand -> shareShot(command.shot)
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
