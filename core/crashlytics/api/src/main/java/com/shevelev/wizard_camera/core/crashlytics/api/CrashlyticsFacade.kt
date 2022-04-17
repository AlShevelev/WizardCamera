package com.shevelev.wizard_camera.core.crashlytics.api

interface CrashlyticsFacade {
    /** */
    fun log(tag: String, string: String)

    /** */
    fun log(ex: Throwable)
}