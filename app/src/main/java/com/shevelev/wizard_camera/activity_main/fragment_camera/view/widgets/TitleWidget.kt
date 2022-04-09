package com.shevelev.wizard_camera.activity_main.fragment_camera.view.widgets

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
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

    fun show(titleText: String?) {
        if(titleText == null) {
            return
        }

        animator?.cancel()

        text = titleText
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
