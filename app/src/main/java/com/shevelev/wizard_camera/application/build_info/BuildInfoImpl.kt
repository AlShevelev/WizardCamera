package com.shevelev.wizard_camera.application.build_info

import android.content.Context
import com.shevelev.wizard_camera.BuildConfig
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.core.build_info.api.BuildInfo
import javax.inject.Inject

class BuildInfoImpl
@Inject
constructor(
    appContext: Context
) : BuildInfo {
    override val crashReportsEnabled: Boolean = BuildConfig.CRASH_REPORTS_ENABLED

    override val type: String = BuildConfig.BUILD_TYPE

    override val flavor: String = BuildConfig.FLAVOR

    override val isDebug: Boolean = BuildConfig.DEBUG

    override val locale: String = appContext.resources.getString(R.string.locale)

    override val appName: String = appContext.getString(R.string.appName)
}