package com.shevelev.wizard_camera.core.crashlytics.impl.device_info

import android.content.Context
import android.content.res.Configuration
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.util.Size
import com.shevelev.wizard_camera.core.utils.ext.minIndexBy
import timber.log.Timber
import java.util.*
import kotlin.math.abs

class DeviceInfoProviderImpl
constructor(
    private val appContext: Context
): com.shevelev.wizard_camera.core.crashlytics.api.device_info.DeviceInfoProvider {
    /**
     * Returns ISO 3166-1 code of country of null if the code can't be detected
     */
    override fun getCountryCode(): String? {
        try {
            val tm = appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            val simCountry = tm.simCountryIso

            // SIM country code is available
            if (simCountry != null && simCountry.length == 2) {
                return simCountry.lowercase(Locale.US)
            }

            // device is not 3G (would be unreliable)
            if (tm.phoneType != TelephonyManager.PHONE_TYPE_CDMA) {
                val networkCountry = tm.networkCountryIso
                if (networkCountry != null && networkCountry.length == 2) { // network country code is available
                    return networkCountry.lowercase(Locale.US)
                }
            }
        } catch (ex: Exception) {
            Timber.e(ex)
        }

        return null
    }

    override fun getSizeInPix(): Size = appContext.resources.displayMetrics.let { Size(it.widthPixels, it.heightPixels) }

    /** */
    override fun getSizeInDp(): Size =
        appContext.resources.displayMetrics.let {
            Size((it.widthPixels / it.density).toInt(), (it.heightPixels / it.density).toInt())
        }

    /** */
    override fun getSizeCategory(): com.shevelev.wizard_camera.core.crashlytics.api.device_info.DisplaySizeCategory =
        appContext.resources.configuration.screenLayout.let {
            when(it and Configuration.SCREENLAYOUT_SIZE_MASK) {
                Configuration.SCREENLAYOUT_SIZE_XLARGE -> com.shevelev.wizard_camera.core.crashlytics.api.device_info.DisplaySizeCategory.XLARGE
                Configuration.SCREENLAYOUT_SIZE_LARGE -> com.shevelev.wizard_camera.core.crashlytics.api.device_info.DisplaySizeCategory.LARGE
                Configuration.SCREENLAYOUT_SIZE_NORMAL -> com.shevelev.wizard_camera.core.crashlytics.api.device_info.DisplaySizeCategory.NORMAL
                Configuration.SCREENLAYOUT_SIZE_SMALL -> com.shevelev.wizard_camera.core.crashlytics.api.device_info.DisplaySizeCategory.SMALL
                else -> com.shevelev.wizard_camera.core.crashlytics.api.device_info.DisplaySizeCategory.UNDEFINED
            }
        }

    /** */
    override fun getDensityCategory(): com.shevelev.wizard_camera.core.crashlytics.api.device_info.DisplayDensityCategory =
        appContext.resources.displayMetrics.let { metrics ->
            val defaultScale = 1F / DisplayMetrics.DENSITY_DEFAULT

            val densityFactors = listOf(
                DisplayMetrics.DENSITY_LOW,
                DisplayMetrics.DENSITY_MEDIUM,
                DisplayMetrics.DENSITY_HIGH,
                DisplayMetrics.DENSITY_XHIGH,
                DisplayMetrics.DENSITY_XXHIGH,
                DisplayMetrics.DENSITY_XXXHIGH)

            val densityIndex = densityFactors.minIndexBy {
                densityFactor, _ -> abs(densityFactor * defaultScale - metrics.scaledDensity)
            }

            return when(densityIndex) {
                0 -> com.shevelev.wizard_camera.core.crashlytics.api.device_info.DisplayDensityCategory.LDPI
                1 -> com.shevelev.wizard_camera.core.crashlytics.api.device_info.DisplayDensityCategory.MDPI
                2 -> com.shevelev.wizard_camera.core.crashlytics.api.device_info.DisplayDensityCategory.HDPI
                3 -> com.shevelev.wizard_camera.core.crashlytics.api.device_info.DisplayDensityCategory.XHDPI
                4 -> com.shevelev.wizard_camera.core.crashlytics.api.device_info.DisplayDensityCategory.XXHDPI
                5 -> com.shevelev.wizard_camera.core.crashlytics.api.device_info.DisplayDensityCategory.XXXHDPI
                else -> throw UnsupportedOperationException("Invalid density index: $densityIndex")
            }
        }
}