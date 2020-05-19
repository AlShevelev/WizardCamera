package com.shevelev.wizard_camera.main_activity.view.gestures

import android.content.Context
import android.graphics.PointF
import android.util.SizeF
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import kotlin.math.abs

class GesturesDetector(context: Context) : GestureDetector.OnGestureListener, ScaleGestureDetector.SimpleOnScaleGestureListener() {
    private val gestureDetector = GestureDetector(context, this)
    private val scaleDetector = ScaleGestureDetector(context, this)

    private var onGestureListener: ((Gesture) -> Unit)? = null

    private lateinit var viewSize: SizeF

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        onGestureListener?.invoke(Pinch(detector.currentSpan))
        return true
    }

    override fun onShowPress(e: MotionEvent?) { /*do nothing*/ }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        onGestureListener?.invoke(Tap(PointF(e.x, e.y), viewSize))
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean = false

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        if(abs(velocityX) < abs(velocityY)) {
            return true
        }

        onGestureListener?.invoke(if(velocityX > 0) FlingLeft else FlingRight )
        return true
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean = false

    override fun onLongPress(e: MotionEvent?) { /*do nothing*/ }

    fun setOnGestureListener(listener: ((Gesture) -> Unit)?){
        onGestureListener = listener
    }

    fun onTouchEvent(view: View, ev: MotionEvent) {
        viewSize = SizeF(view.width.toFloat(), view.height.toFloat())
        gestureDetector.onTouchEvent(ev)
        scaleDetector.onTouchEvent(ev)
    }
}