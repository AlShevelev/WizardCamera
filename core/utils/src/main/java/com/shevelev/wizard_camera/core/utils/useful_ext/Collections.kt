package com.shevelev.wizard_camera.core.utils.useful_ext

/**
 * Returns an index of the first element yielding the smallest value of the given function or `null` if there are no elements.
 */
inline fun <T, R : Comparable<R>> Iterable<T>.minIndexBy(selector: (T, Int) -> R): Int? {
    val iterator = iterator()

    if (!iterator.hasNext()) {
        return null
    }

    var minIndex = 0
    var currentIndex = minIndex

    var minValue = selector(iterator.next(), currentIndex)

    while (iterator.hasNext()) {
        val v = selector(iterator.next(), currentIndex)
        currentIndex++

        if (minValue > v) {
            minValue = v
            minIndex = currentIndex
        }
    }
    return minIndex
}