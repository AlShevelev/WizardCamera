package com.shevelev.wizard_camera.core.build_info.api

/**
 * It's a wrapper around the application BuildConfig
 */
interface BuildInfo {
    val crashReportsEnabled : Boolean

    val type: String

    val flavor : String

    val isDebug: Boolean

    val locale : String
}