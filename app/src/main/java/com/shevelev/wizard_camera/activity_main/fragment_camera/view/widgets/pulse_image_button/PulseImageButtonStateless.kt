package com.shevelev.wizard_camera.activity_main.fragment_camera.view.widgets.pulse_image_button

import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.core.camera_gl.shared.animation.AnimationUtils

@SuppressLint("AppCompatCustomView")
class PulseImageButtonStateless
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr)  {

    private var animSet: AnimatorSet? = null

    private val minScale = 0.8f
    private val maxScale = 1f

    private var onClickListener: (() -> Unit)? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PulseImageButtonStateless)
        setImageDrawable(typedArray.getDrawable(R.styleable.PulseImageButtonStateless_pulse_button_icon))
        typedArray.recycle()

        scaleType = ScaleType.FIT_XY
        scaleX = maxScale
        scaleY = maxScale

        super.setOnClickListener { processClick() }
    }

    fun setOnPulseButtonClickListener(listener: (() -> Unit)?) {
        onClickListener = listener
    }

    private fun processClick() {
        animSet?.cancel()

        scaleX = maxScale
        scaleY = maxScale

        val duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        val reduceAnimation = AnimationUtils.getFloatAnimator(
            duration = duration,
            from = maxScale,
            to = minScale,
            updateListener = {
                scaleX = it
                scaleY = it
            }
        )
        val increaseAnimation = AnimationUtils.getFloatAnimator(
            duration = duration,
            from = minScale,
            to = maxScale,
            updateListener = {
                scaleX = it
                scaleY = it
            },
            completeListener = {
                onClickListener?.invoke()
            }
        )

        animSet = AnimatorSet()
            .apply {
                playSequentially(reduceAnimation, increaseAnimation)
                start()
            }
    }
}
