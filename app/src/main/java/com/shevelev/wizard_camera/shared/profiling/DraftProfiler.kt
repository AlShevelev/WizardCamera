package com.shevelev.wizard_camera.shared.profiling

import timber.log.Timber

object DraftProfiler {
    fun profile(label: String? = null, action: () -> Unit) = profile<Unit>(label, action)

    fun <T>profile(label: String? = null, action: () -> T): T {
        val start = System.currentTimeMillis()
        val result = action()
        val end = System.currentTimeMillis()

        val delta = end - start

        val seconds = delta / 1000
        val milliseconds = delta % 1000

        Timber.tag("DRAFT_PROFILER").d("$label: $seconds[s]; $milliseconds[ms]")

        return result
    }
}