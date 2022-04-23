package com.shevelev.wizard_camera.activity_gallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.activity_gallery.di.GalleryActivityScope
import com.shevelev.wizard_camera.core.ui_utils.ext.hideSystemUI

class GalleryActivity : AppCompatActivity() {
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

    override fun onDestroy() {
        if(isFinishing) {
            GalleryActivityScope.closeScope()
        }
        super.onDestroy()
    }
}
