package com.shevelev.wizard_camera.utils.crashlytics

interface CrashlyticsFacade {
    /** */
    fun log(tag: String, string: String)

    /** */
    fun log(ex: Throwable)
}