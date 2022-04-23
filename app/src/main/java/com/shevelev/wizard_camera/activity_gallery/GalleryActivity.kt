package com.shevelev.wizard_camera.activity_gallery

import android.os.Bundle
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.activity_gallery.di.GalleryActivityComponent
import com.shevelev.wizard_camera.activity_gallery.di.GalleryActivityScope
import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.core.ui_utils.ext.hideSystemUI
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view.ActivityBase

class GalleryActivity : ActivityBase() {
    override fun inject() = App.injections.get<GalleryActivityComponent>().inject(this)

    override fun releaseInjection() {
        App.injections.release<GalleryActivityComponent>()
        GalleryActivityScope.closeScope()
    }

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
