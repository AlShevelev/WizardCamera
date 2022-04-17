package com.shevelev.wizard_camera.core.logger

import android.util.Log
import com.shevelev.wizard_camera.core.crashlytics.api.CrashlyticsFacade
import timber.log.Timber

class TimberTreeRelease(private val crashlytics: CrashlyticsFacade): Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if(t != null) {
            t.printStackTrace()
            crashlytics.log(t)
            return
        }

        Log.println(priority, tag, message)
    }
}