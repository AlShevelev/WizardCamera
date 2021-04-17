package com.shevelev.wizard_camera.shared.glide

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.target.Target
import java.io.File

typealias GlideTarget = Target<*>

fun ImageView.load(file: File, @DrawableRes defaultRes: Int): GlideTarget = load(file, defaultRes, null)

fun ImageView.load(file: File, @DrawableRes defaultRes: Int, transformation: Transformation<Bitmap>?): GlideTarget =
    Glide
        .with(this)
        .load(file)
        .apply {
            transformation?.let { transform(it) }
        }
        .fallback(defaultRes)
        .error(defaultRes)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .into(this)

fun ImageView.loadCircle(@DrawableRes imageRes: Int): GlideTarget =
    Glide
        .with(this)
        .load(imageRes)
        .transform(CircleCrop())
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .into(this)

fun GlideTarget.clear(context: Context) {
    if(context is Activity && context.isDestroyed){
        return
    }

    Glide.with(context).clear(this)
}