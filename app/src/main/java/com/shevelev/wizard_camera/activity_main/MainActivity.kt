package com.shevelev.wizard_camera.activity_main

import android.os.Bundle
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.application.App
import com.shevelev.wizard_camera.activity_main.di.MainActivityComponent
import com.shevelev.wizard_camera.core.ui_utils.ext.hideSystemUI
import com.shevelev.wizard_camera.core.ui_utils.mvvm.view.ActivityBase

class MainActivity : ActivityBase() {
    override fun inject() = App.injections.get<MainActivityComponent>().inject(this)

    override fun releaseInjection() = App.injections.release<MainActivityComponent>()

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