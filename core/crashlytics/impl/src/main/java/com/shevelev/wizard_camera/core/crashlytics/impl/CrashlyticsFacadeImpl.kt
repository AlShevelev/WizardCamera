package com.shevelev.wizard_camera.core.crashlytics.impl

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.shevelev.wizard_camera.core.build_info.api.BuildInfo
import com.shevelev.wizard_camera.core.crashlytics.api.CrashlyticsFacade
import com.shevelev.wizard_camera.core.crashlytics.api.device_info.DeviceInfoProvider

internal class CrashlyticsFacadeImpl(
    deviceInfoProvider: DeviceInfoProvider,
    buildInfo: BuildInfo
): CrashlyticsFacade {

    private val enabled = buildInfo.crashReportsEnabled

    init {
        doCall {
            FirebaseCrashlytics.getInstance().setCustomKey("BUILD_TYPE", buildInfo.type)
            FirebaseCrashlytics.getInstance().setCustomKey("BUILD_FLAVOR", buildInfo.flavor)
            FirebaseCrashlytics.getInstance().setCustomKey("IS_DEBUG_BUILD", buildInfo.isDebug)

            FirebaseCrashlytics.getInstance().setCustomKey("LOCALE", buildInfo.locale)

            FirebaseCrashlytics.getInstance().setCustomKey("COUNTRY", deviceInfoProvider.getCountryCode() ?: "")

            deviceInfoProvider.getSizeInPix().apply {
                FirebaseCrashlytics.getInstance().setCustomKey("DISPLAY_SIZE_PIXELS", "${this.width}x${this.height} [pix]")
            }

            deviceInfoProvider.getSizeInDp().apply {
                FirebaseCrashlytics.getInstance().setCustomKey("DISPLAY_SIZE_DP", "${this.width}x${this.height} [dp]")
            }

            deviceInfoProvider.getDensityCategory().apply {
                FirebaseCrashlytics.getInstance().setCustomKey("DISPLAY_DENSITY_CATEGORY", this.toString())
            }

            deviceInfoProvider.getSizeCategory().apply {
                FirebaseCrashlytics.getInstance().setCustomKey("DISPLAY_SIZE_CATEGORY", this.toString())
            }
        }
    }

    override fun log(tag: String, string: String) =
        doCall {
            FirebaseCrashlytics.getInstance().log("$tag: $string")
        }

    override fun log(ex: Throwable) =
        doCall {
            val typeName = ex.javaClass.simpleName

            val callInfo = if(ex.stackTrace.isNotEmpty()) {
                ex.stackTrace[0].let { stackItem ->
                    stackItem.className.let {
                        "${it.substring(it.lastIndexOf(".") + 1, it.length)}::${stackItem.methodName} at ${stackItem.lineNumber}"
                    }
                }
            } else {
                "no stack trace data"
            }


            FirebaseCrashlytics.getInstance().log("EXCEPTION Type: $typeName, message: ${ex.message} [$callInfo]")
        }

    private fun doCall(call: () -> Unit) {
        if (enabled) {
            call()
        }
    }
}