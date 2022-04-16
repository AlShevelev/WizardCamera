package com.shevelev.wizard_camera.core.camera_gl.shared.animation

import android.animation.Animator

open class AnimatorListenerBase : Animator.AnimatorListener {
    override fun onAnimationRepeat(animation: Animator?) { }

    override fun onAnimationEnd(animation: Animator?) { }

    override fun onAnimationCancel(animation: Animator?) { }

    override fun onAnimationStart(animation: Animator?) { }
}