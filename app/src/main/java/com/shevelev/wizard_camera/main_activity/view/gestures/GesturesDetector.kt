package com.shevelev.wizard_camera.main_activity.view.gestures

import android.content.Context
import android.graphics.PointF
import android.util.SizeF
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class GesturesDetector(context: Context) : GestureDetector.OnGestureListener {
    private val gestureDetector = GestureDetector(context, this)

    private var onGestureListener: ((Gesture) -> Unit)? = null

    private lateinit var viewSize: SizeF

    override fun onShowPress(e: MotionEvent?) { /*do nothing*/ }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        onGestureListener?.invoke(Gesture.Tap(PointF(e.x, e.y), viewSize))
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean = false

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        if(abs(velocityX) < abs(velocityY)) {
            return true
        }

        onGestureListener?.invoke(if(velocityX > 0) Gesture.FlingLeft else Gesture.FlingRight )
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
    }
}