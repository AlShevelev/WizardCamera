package com.shevelev.wizard_camera.activity_main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.core.ui_utils.ext.hideSystemUI

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }
}