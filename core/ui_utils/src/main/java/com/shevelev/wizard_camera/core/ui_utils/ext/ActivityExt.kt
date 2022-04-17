package com.shevelev.wizard_camera.core.ui_utils.ext

import android.app.Activity
import android.view.WindowManager

fun Activity.hideSystemUI() {
    // To make Navigation bar half-transparent
    window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

//    window.decorView.systemUiVisibility =
//        (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//            or View.SYSTEM_UI_FLAG_FULLSCREEN)
}
