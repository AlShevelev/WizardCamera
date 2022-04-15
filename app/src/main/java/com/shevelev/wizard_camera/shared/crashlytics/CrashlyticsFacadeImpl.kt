package com.shevelev.wizard_camera.shared.crashlytics

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.shevelev.wizard_camera.BuildConfig
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.core.utils.crashlytics.CrashlyticsFacade
import com.shevelev.wizard_camera.core.utils.device_info.DeviceInfoProvider
import javax.inject.Inject

class CrashlyticsFacadeImpl
@Inject
constructor(
    appContext: Context,
    deviceInfoProvider: DeviceInfoProvider
): CrashlyticsFacade {

    private val enabled = BuildConfig.CRASH_REPORTS_ENABLED

    init {
        doCall {
            FirebaseCrashlytics.getInstance().setCustomKey("BUILD_TYPE", BuildConfig.BUILD_TYPE)
            FirebaseCrashlytics.getInstance().setCustomKey("BUILD_FLAVOR", BuildConfig.FLAVOR)
            FirebaseCrashlytics.getInstance().setCustomKey("IS_DEBUG_BUILD", BuildConfig.DEBUG)

            FirebaseCrashlytics.getInstance().setCustomKey("LOCALE", appContext.resources.getString(R.string.locale))

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