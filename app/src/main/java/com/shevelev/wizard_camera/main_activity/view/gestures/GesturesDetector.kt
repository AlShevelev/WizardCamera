package com.shevelev.wizard_camera.main_activity.view.gestures

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import kotlin.math.abs

class GesturesDetector(context: Context) : GestureDetector.OnGestureListener {
    private val gestureDetector = GestureDetector(context, this)

    private var onGestureListener: ((Gesture) -> Unit)? = null

    override fun onShowPress(e: MotionEvent?) { /*do nothing*/ }

    override fun onSingleTapUp(e: MotionEvent?): Boolean = false

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

    fun onTouchEvent(ev: MotionEvent) {
        gestureDetector.onTouchEvent(ev)
    }
}