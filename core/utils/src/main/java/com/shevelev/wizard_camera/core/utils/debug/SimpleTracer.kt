package com.shevelev.wizard_camera.core.utils.debug

import timber.log.Timber
import java.text.DecimalFormat
import java.util.concurrent.atomic.AtomicLong

object SimpleTracer {
    private const val TAG = "SIMPLE_TRACER"

    private var traceTime = AtomicLong(Long.MIN_VALUE)

    private val timeFormatter = createTimeFormatter()

    fun start(message: String) {
        val now = System.nanoTime()
        traceTime.set(now)

        Timber.tag(TAG).d("[0] $message")
    }

    fun trace(message: String) {
        val now = System.nanoTime()
        val old = traceTime.getAndSet(now)

        val delta = now - old

        Timber.tag(TAG).d("[${timeFormatter.format(delta)}] $message")
    }

    private fun createTimeFormatter() : DecimalFormat =
        DecimalFormat().apply {
            groupingSize = 3
        }
}