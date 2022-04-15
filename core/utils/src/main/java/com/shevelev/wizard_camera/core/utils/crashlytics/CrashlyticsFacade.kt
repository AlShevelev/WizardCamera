package com.shevelev.wizard_camera.core.utils.crashlytics

interface CrashlyticsFacade {
    /** */
    fun log(tag: String, string: String)

    /** */
    fun log(ex: Throwable)
}