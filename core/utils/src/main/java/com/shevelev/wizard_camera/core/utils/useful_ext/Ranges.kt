package com.shevelev.wizard_camera.core.utils.useful_ext

import android.util.Range

fun <T: Comparable<T>>T.fitInRange(range: Range<T>): T =
    when {
        this < range.lower -> range.lower
        this > range.upper -> range.upper
        else -> this
    }

fun Float.reduceToRange(rangeFrom: Range<Float>, rangeTo: Range<Float>): Float =
    when {
        this == rangeFrom.lower -> rangeTo.lower
        this == rangeFrom.upper -> rangeTo.upper
        else -> {
            val placeInRange = (this - rangeFrom.lower) / (rangeFrom.upper - rangeFrom.lower)
            ((rangeTo.upper - rangeTo.lower) * placeInRange) + rangeTo.lower
        }
    }