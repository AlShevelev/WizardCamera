package com.shevelev.wizard_camera.activity_gallery

import android.os.Bundle
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.activity_gallery.di.GalleryActivityComponent
import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.core.camera_gl.shared.mvvm.view.ActivityBase
import com.shevelev.wizard_camera.core.camera_gl.shared.ui_utils.hideSystemUI

class GalleryActivity : ActivityBase() {
    override fun inject() = App.injections.get<GalleryActivityComponent>().inject(this)

    override fun releaseInjection() = App.injections.release<GalleryActivityComponent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }
}
