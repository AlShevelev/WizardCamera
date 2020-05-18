package com.shevelev.wizard_camera.main_activity.view.widgets

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import com.shevelev.wizard_camera.shared.animation.AnimationUtils

class TitleWidget
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr)  {

    private var animator: ValueAnimator? = null

    fun show(@StringRes titleResId: Int) {
        animator?.cancel()

        setText(titleResId)
        alpha = 1f

        animator = AnimationUtils.getFloatAnimator(
            duration = 500,
            forward = false,
            updateListener = { alpha -> this.alpha = alpha }
        ).apply {
            startDelay = 2000L
            start()
        }
    }
}
