package com.shevelev.wizard_camera.core.utils.device_info

import android.content.Context
import android.content.res.Configuration
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.util.Size
import com.shevelev.wizard_camera.core.utils.useful_ext.minIndexBy
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

class DeviceInfoProviderImpl
@Inject
constructor(
    private val appContext: Context
): DeviceInfoProvider {
    /**
     * Returns ISO 3166-1 code of country of null if the code can't be detected
     */
    override fun getCountryCode(): String? {
        try {
            val tm = appContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            val simCountry = tm.simCountryIso

            // SIM country code is available
            if (simCountry != null && simCountry.length == 2) {
                return simCountry.toLowerCase(Locale.US)
            }

            // device is not 3G (would be unreliable)
            if (tm.phoneType != TelephonyManager.PHONE_TYPE_CDMA) {
                val networkCountry = tm.networkCountryIso
                if (networkCountry != null && networkCountry.length == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US)
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
    override fun getSizeCategory(): DisplaySizeCategory =
        appContext.resources.configuration.screenLayout.let {
            when(it and Configuration.SCREENLAYOUT_SIZE_MASK) {
                Configuration.SCREENLAYOUT_SIZE_XLARGE -> DisplaySizeCategory.XLARGE
                Configuration.SCREENLAYOUT_SIZE_LARGE -> DisplaySizeCategory.LARGE
                Configuration.SCREENLAYOUT_SIZE_NORMAL -> DisplaySizeCategory.NORMAL
                Configuration.SCREENLAYOUT_SIZE_SMALL -> DisplaySizeCategory.SMALL
                else -> DisplaySizeCategory.UNDEFINED
            }
        }

    /** */
    override fun getDensityCategory(): DisplayDensityCategory =
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
                0 -> DisplayDensityCategory.LDPI
                1 -> DisplayDensityCategory.MDPI
                2 -> DisplayDensityCategory.HDPI
                3 -> DisplayDensityCategory.XHDPI
                4 -> DisplayDensityCategory.XXHDPI
                5 -> DisplayDensityCategory.XXXHDPI
                else -> throw UnsupportedOperationException("Invalid density index: $densityIndex")
            }
        }
}