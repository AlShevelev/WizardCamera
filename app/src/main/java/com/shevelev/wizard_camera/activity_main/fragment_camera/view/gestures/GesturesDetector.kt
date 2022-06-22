package com.shevelev.wizard_camera.activity_main.fragment_camera.view.gestures

import android.content.Context
import android.graphics.PointF
import android.util.SizeF
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View

private const val LAST_SCALE_FACTORS_CAPACITY = 15

class GesturesDetector(context: Context) : GestureDetector.OnGestureListener,
    ScaleGestureDetector.SimpleOnScaleGestureListener() {
    private val gestureDetector = GestureDetector(context, this)
    private val scaleDetector = ScaleGestureDetector(context, this)

    private var onGestureListener: ((Gesture) -> Unit)? = null

    private lateinit var viewSize: SizeF

    private val lastScaleFactors = ArrayDeque<Float>(LAST_SCALE_FACTORS_CAPACITY)

    override fun onScaleEnd(detector: ScaleGestureDetector?) {
        super.onScaleEnd(detector)
        lastScaleFactors.clear()
    }

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        val scaleFactor = detector.scaleFactor

        if (lastScaleFactors.size <= LAST_SCALE_FACTORS_CAPACITY) {
            lastScaleFactors.add(scaleFactor)
        } else {
            lastScaleFactors.removeLast()
            lastScaleFactors.addFirst(scaleFactor)
        }

        val fastScaleFactor = (lastScaleFactors.sum() / lastScaleFactors.size)
            .let {
                when {
                    scaleFactor > 1f -> scaleFactor * 1.0125f
                    scaleFactor < 1f -> scaleFactor * 0.9875f
                    else -> scaleFactor
                }
            }

        onGestureListener?.invoke(Pinch(fastScaleFactor))
        return true
    }

    override fun onShowPress(e: MotionEvent?) { /*do nothing*/
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        onGestureListener?.invoke(Tap(PointF(e.x, e.y), viewSize))
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean = false

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        return true
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean = false

    override fun onLongPress(e: MotionEvent?) { /*do nothing*/
    }

    fun setOnGestureListener(listener: ((Gesture) -> Unit)?) {
        onGestureListener = listener
    }

    fun onTouchEvent(view: View, ev: MotionEvent) {
        viewSize = SizeF(view.width.toFloat(), view.height.toFloat())
        gestureDetector.onTouchEvent(ev)
        scaleDetector.onTouchEvent(ev)
    }
}