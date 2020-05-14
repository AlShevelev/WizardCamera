package com.shevelev.wizard_camera.shared.logging

import android.util.Log
import timber.log.Timber

class TimberTreeDebug: Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if(t != null) {
            t.printStackTrace()
            return
        }

        Log.println(priority, tag, message)
    }
}