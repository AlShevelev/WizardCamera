package com.shevelev.my_footprints_remastered.utils.resources

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Size
import androidx.annotation.*
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.MessageFormat

fun Context.getStringFormatted(@StringRes resId: Int, vararg args: Any): String {
    return MessageFormat.format(getString(resId), *args)
}

fun Context.getRawString(@RawRes resId: Int): String =
    try {
        resources.openRawResource(resId).use { inputStream ->
            ByteArrayOutputStream().use { outputStream ->
                var i = inputStream.read()
                while (i != -1) {
                    outputStream.write(i)
                    i = inputStream.read()
                }

                outputStream.toString()
            }
        }
    } catch (ex: IOException) {
        Timber.e(ex)
        throw ex
    }

fun Context.getDimension(@DimenRes resId: Int): Float = this.resources.getDimension(resId)

fun Context.isPortrait(): Boolean {
    return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
}

/**
 * Get screen size in pixels
 */
fun Context.getScreenSize(): Size =
    Resources.getSystem().displayMetrics.let {
        Size(it.widthPixels, it.heightPixels)
    }
