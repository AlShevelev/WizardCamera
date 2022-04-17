package com.shevelev.wizard_camera.core.ui_utils.animation

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator

object AnimationUtils {
    inline fun getFloatAnimator(
        forward: Boolean = true,
        from: Float = 0f,
        to: Float = 1f,
        duration: Long = 200,
        delay: Long = 0,
        interpolator: TimeInterpolator? = null,
        crossinline startListener: () -> Unit = {},
        crossinline updateListener: (progress: Float) -> Unit,
        crossinline completeListener: () -> Unit = {}
    ): ValueAnimator {
        val a = if (forward) {
            ValueAnimator.ofFloat(from, to)
        } else {
            ValueAnimator.ofFloat(to, from)
        }

        a.duration = duration
        a.startDelay = delay
        interpolator?.let { a.interpolator = it }

        a.addUpdateListener { updateListener(it.animatedValue as Float) }

        a.addListener(object : AnimatorListenerBase() {
            override fun onAnimationStart(animation: Animator?) {
                startListener()
            }

            override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                completeListener()
            }
        })
        return a
    }
}